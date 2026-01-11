package ru.p4ejlov0d.galateahunter.repo.impl;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import ru.p4ejlov0d.galateahunter.model.Shard;
import ru.p4ejlov0d.galateahunter.repo.ShardRepo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.LOGGER;
import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

public class ShardRepoImpl implements ShardRepo {
    private static ShardRepo instance;
    private final Path REPO_DIR = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + "/repo");
    // skyshards
    private final String remoteRepoPath = "https://github.com/Campionnn/SkyShards.git";
    private final String remoteDataRepoPath = "https://skyshards.com/fusion-data.json";

    private Map<String, Shard> shards;

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
        File images = new File(REPO_DIR.resolve("images").toUri());
        File shardIcons = images.toPath().resolve("public/shardIcons").toFile();

        if (!Files.isDirectory(shardIcons.toPath()) || shardIcons.listFiles().length == 0) {
            LOGGER.info("Cloning skyshards repository to {}", images.getAbsolutePath());
            cloneRepoWithImages(images);
        }

        return shardIcons.listFiles();
    }

    private void cloneRepoWithImages(File images) {
        try {
            FileUtils.deleteDirectory(images);
        } catch (IOException e) {
            LOGGER.error("Failed to delete directory {}", images.getAbsolutePath(), e);
        }

        images.mkdirs();

        try (Git git = Git.cloneRepository()
                .setURI(remoteRepoPath)
                .setDirectory(images)
                .setBranchesToClone(List.of("refs/heads/master"))
                .setBranch("master")
                .setDepth(1)
                .call()
        ) {
            LOGGER.info("Successfully skyshards cloned repository to {}", images.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.error("An error occurred while trying to download images", e);
        }
    }

    @Override
    public void getShardData() {

    }
}
