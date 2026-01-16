package ru.p4ejlov0d.galateahunter.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.Identifier;
import ru.p4ejlov0d.galateahunter.model.Shard;
import ru.p4ejlov0d.galateahunter.repo.ShardRepo;
import ru.p4ejlov0d.galateahunter.repo.impl.ShardRepoImpl;

import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.LOGGER;
import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;
import static ru.p4ejlov0d.galateahunter.repo.impl.ShardRepoImpl.imagesRootPath;

public class ShardManager {
    private static ShardManager instance;
    private final ShardRepo shardRepo = ShardRepoImpl.getInstance();
    private ResourcePack resourcePack;

    private ShardManager() {
    }

    public static ShardManager getInstance() {
        return instance == null ? instance = new ShardManager() : instance;
    }

    public CompletableFuture<Void> load() {
        return prepare().thenCompose(this::apply);
    }

    private CompletableFuture<Map<String, Pair<String, File>>> prepare() {
        LOGGER.info("Preparing shards data");

        CompletableFuture<File[]> prepareShardImages = CompletableFuture.supplyAsync(shardRepo::getShardImages);
        CompletableFuture<File> prepareShardData = CompletableFuture.supplyAsync(shardRepo::getShardData);
        CompletableFuture<Map<String, Pair<String, File>>> combine = prepareShardImages.thenApplyAsync(files -> {
            Map<String, File> idToImage = new HashMap<>();

            for (File file : files) {
                idToImage.put(file.getName().split("\\.")[0], file);
            }

            return idToImage;
        }).thenCombine(prepareShardData, (idToImage, data) -> {
            Map<String, Pair<String, File>> jsonIdImage = new HashMap<>();

            try (BufferedReader reader = Files.newBufferedReader(data.toPath())) {
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.contains("\": { \"name\": \"")) {
                        String id = line.trim().substring(1).split("\"")[0].toLowerCase();

                        jsonIdImage.put(line.trim(), Pair.of(id, Optional.ofNullable(idToImage.get(id)).orElse(new File(imagesRootPath.resolve("assets/" + MOD_ID + "/").toFile() + id + ".png"))));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return jsonIdImage;
        });

        combine.thenRun(() -> LOGGER.info("Preparing shards data complete"));

        return combine;
    }

    private <T> CompletableFuture<Void> apply(T data) {
        return CompletableFuture.runAsync(() -> {
            try {
                LOGGER.info("Ready to apply loaded shard data");

                for (Map.Entry<String, Pair<String, File>> entry : ((HashMap<String, Pair<String, File>>) data).entrySet()) {
                    String json = entry.getKey();
                    String id = entry.getValue().getLeft();
                    File image = entry.getValue().getRight();

                    LOGGER.debug("Creating new shard instance {}", id);

                    Shard shard = new ObjectMapper().readValue(json.substring(6, json.lastIndexOf("},") == -1 ? json.length() : json.lastIndexOf("},") + 1), Shard.class);
                    Identifier texture = Identifier.of(MOD_ID, image.getName());

                    shard.setId(id);
                    shard.setTexture(texture);
                    shard.setName(shard.getName() + " Shard");

                    shardRepo.getShards().put(id, shard);

                    LOGGER.debug("Successfully created shard instance {}", id);
                }

                Field basePaths = resourcePack.getClass().getDeclaredField("basePaths");
                basePaths.setAccessible(true);
                Path basePath = ((List<Path>) basePaths.get(resourcePack)).getFirst();
                basePaths.set(resourcePack, new ArrayList<>());
                List<Path> paths = ((List<Path>) basePaths.get(resourcePack));

                paths.add(basePath);
                paths.add(imagesRootPath);

                LOGGER.info("Successfully applied shard data");
            } catch (Exception e) {
                LOGGER.warn("Failed to handle shard", e);
            }
        });
    }

    public void setResourcePack(ResourcePack resourcePack) {
        this.resourcePack = resourcePack;
    }
}
