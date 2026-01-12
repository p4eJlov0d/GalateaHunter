package ru.p4ejlov0d.galateahunter.utils;

import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Pair;
import ru.p4ejlov0d.galateahunter.repo.ShardRepo;
import ru.p4ejlov0d.galateahunter.repo.impl.ShardRepoImpl;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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
            Map<String, Pair<String, File>> result = (HashMap) data;
            Map<Identifier, Resource> resources = manager.findResources("textures/gui", identifier -> identifier.getNamespace().equals(MOD_ID) && identifier.getPath().endsWith(".png"));

            try (ResourcePack resourcePack = manager.streamResourcePacks()
                    .filter(resourcePack1 -> resourcePack1.getId().equals(MOD_ID))
                    .findFirst().orElse(null)
            ) {
                for (Map.Entry<String, Pair<String, File>> entry : result.entrySet()) {
                    String json = entry.getKey();
                    String id = entry.getValue().getLeft();
                    File image = entry.getValue().getRight();

                    resources.put(Identifier.of(MOD_ID, "textures/gui/" + image.getName().toLowerCase()), new Resource(resourcePack, () -> Files.newInputStream(image.toPath())));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
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
