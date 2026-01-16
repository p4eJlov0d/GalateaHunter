package ru.p4ejlov0d.galateahunter.repo.impl;

import org.apache.commons.io.FileUtils;
import ru.p4ejlov0d.galateahunter.model.Shard;
import ru.p4ejlov0d.galateahunter.repo.ShardRepo;
import ru.p4ejlov0d.galateahunter.repo.remote.RemoteRepository;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.*;

public class ShardRepoImpl implements ShardRepo {
    public static final Path imagesRootPath = MOD_CONFIG_DIR.resolve("images");
    public static final Path dataRootPath = MOD_CONFIG_DIR.resolve("data");
    private static ShardRepo instance;
    private final RemoteRepository REMOTE_REPOSITORY = RemoteRepository.getInstance();
    // skyshards
    private final URI remoteDataPath = URI.create("https://skyshards.com/fusion-data.json");

    private final Map<String, Shard> SHARDS = new HashMap<>();

    private ShardRepoImpl() {
    }

    public static ShardRepo getInstance() {
        if (instance == null) {
            instance = new ShardRepoImpl();
        }
        return instance;
    }

    @Override
    public File[] getShardImages() {
        File shardIcons = imagesRootPath.resolve("assets/" + MOD_ID + "/").toFile();

        if (!Files.isDirectory(shardIcons.toPath()) || REMOTE_REPOSITORY.isNeedUpdate())
            REMOTE_REPOSITORY.cloneRepoWithImages();

        return Arrays.stream(shardIcons.listFiles())
                .filter(file -> file.isFile() && file.getName().endsWith(".png"))
                .toArray(File[]::new);
    }

    @Override
    public File getShardData() {
        File dataFile = new File(dataRootPath.resolve("fusion-data.json").toUri());

        if (!Files.exists(dataFile.toPath())) {
            try {
                LOGGER.info("Downloading shard data to {}", dataFile.getAbsolutePath());
                FileUtils.copyURLToFile(remoteDataPath.toURL(), dataFile, 10000, 10000);
                LOGGER.info("Successfully downloaded shard data to {}", dataFile.getAbsolutePath());
            } catch (Exception e) {
                LOGGER.error("An error occurred while trying to download shard data", e);
            }
        }

        return dataFile;
    }

    @Override
    public Map<String, Shard> getShards() {
        return SHARDS;
    }
}
