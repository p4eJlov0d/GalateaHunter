package ru.p4ejlov0d.galateahunter.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Pair;
import ru.p4ejlov0d.galateahunter.model.Shard;
import ru.p4ejlov0d.galateahunter.repo.ShardRepo;
import ru.p4ejlov0d.galateahunter.repo.impl.ShardRepoImpl;

import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.LOGGER;
import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

public class ShardsReloader<T> implements SimpleResourceReloadListener<T> {
    private final ShardRepo shardRepo = ShardRepoImpl.getInstance();

    /**
     * Asynchronously process and load resource-based data. The code
     * must be thread-safe and not modify game state!
     *
     * @param manager  The resource manager used during reloading.
     * @param executor The executor which should be used for this stage.
     * @return A CompletableFuture representing the "data loading" stage.
     */
    @Override
    public CompletableFuture<T> load(ResourceManager manager, Executor executor) {
        LOGGER.info("Preparing shards data");

        CompletableFuture<File[]> prepareShardImages = CompletableFuture.supplyAsync(shardRepo::getShardImages, executor);
        CompletableFuture<File> prepareShardData = CompletableFuture.supplyAsync(shardRepo::getShardData, executor);
        CompletableFuture combine = prepareShardImages.thenComposeAsync(files -> {
            Map<String, File> idToImage = new HashMap<>();

            for (File file : files) {
                idToImage.put(file.getName().split("\\.")[0], file);
            }

            return CompletableFuture.completedFuture(idToImage);
        }, executor).thenCombineAsync(prepareShardData, (idToImage, data) -> {
            Map<String, Pair<String, File>> jsonIdImage = new HashMap<>();

            try (BufferedReader reader = Files.newBufferedReader(data.toPath())) {
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.contains("\": { \"name\": \"")) {
                        String id = line.trim().substring(1).split("\"")[0];

                        jsonIdImage.put(line.trim(), Pair.of(id, idToImage.get(id)));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return jsonIdImage;
        }, executor);

        combine.thenRun(() -> LOGGER.info("Preparing shards data complete"));

        return combine;
    }

    /**
     * Synchronously apply loaded data to the game state.
     *
     * @param data
     * @param manager  The resource manager used during reloading.
     * @param executor The executor which should be used for this stage.
     * @return A CompletableFuture representing the "data applying" stage.
     */
    @Override
    public CompletableFuture<Void> apply(T data, ResourceManager manager, Executor executor) {
        return CompletableFuture.runAsync(() -> {
            try (ResourcePack resourcePack = manager.streamResourcePacks()
                    .filter(resourcePack1 -> resourcePack1.getId().equals(MOD_ID))
                    .findFirst().orElse(null)
            ) {
                LOGGER.info("Ready to apply loaded shard data");

                Path imagesPath = null;

                for (Map.Entry<String, Pair<String, File>> entry : ((HashMap<String, Pair<String, File>>) data).entrySet()) {
                    String json = entry.getKey();
                    String id = entry.getValue().getLeft();
                    File image = entry.getValue().getRight();

                    LOGGER.debug("Creating new shard instance {}", id);

                    Shard shard = new ObjectMapper().readValue(json.substring(6, json.lastIndexOf("},") == -1 ? json.length() : json.lastIndexOf("},") + 1), Shard.class);
                    Identifier texture = Identifier.of(MOD_ID, id.toLowerCase() + ".png");

                    shard.setId(id);
                    shard.setTexture(texture);
                    shard.setName(shard.getName() + " Shard");

                    shardRepo.getShards().put(id, shard);

                    LOGGER.debug("Successfully created shard instance {}", id);

                    imagesPath = Path.of(image.getParent());

                    Path file = new File(imagesPath.resolve("assets/" + MOD_ID).resolve(image.getName().toLowerCase()).toUri()).toPath();

                    try {
                        LOGGER.debug("Copying shard image from {}, {}", image, file);

                        imagesPath.resolve("assets/" + MOD_ID).toFile().mkdirs();
                        Files.createFile(file);
                        Files.copy(image.toPath(), file, StandardCopyOption.REPLACE_EXISTING);

                        LOGGER.debug("Successfully copied shard image {}", image);
                    } catch (FileAlreadyExistsException e) {
                        LOGGER.debug("Image already exists {} at path {}", image, file);
                    }
                }

                Field basePaths = resourcePack.getClass().getDeclaredField("basePaths");
                basePaths.setAccessible(true);
                Path basePath = ((List<Path>) basePaths.get(resourcePack)).getFirst();
                basePaths.set(resourcePack, new ArrayList<>());
                List<Path> paths = ((List<Path>) basePaths.get(resourcePack));

                paths.add(basePath);
                paths.add(imagesPath);

                LOGGER.info("Successfully applied shard data");
            } catch (Exception e) {
                LOGGER.warn("Failed to handle shard", e);
            }
        }, executor);
    }

    /**
     * @return The unique identifier of this listener.
     */
    @Override
    public Identifier getFabricId() {
        return Identifier.tryParse(MOD_ID, "load_shard_data");
    }
}
