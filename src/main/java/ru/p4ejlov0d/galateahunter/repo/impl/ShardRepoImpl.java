package ru.p4ejlov0d.galateahunter.repo.impl;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import ru.p4ejlov0d.galateahunter.model.Shard;
import ru.p4ejlov0d.galateahunter.repo.ShardRepo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.LOGGER;
import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

public class ShardRepoImpl implements ShardRepo {
    private static ShardRepo instance;
    private final Path REPO_DIR = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + "/repo");
    // skyshards
    private final String remoteRepoPath = "https://github.com/Campionnn/SkyShards.git";
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
        File images = new File(REPO_DIR.resolve("images").toUri());
        File shardIcons = images.toPath().resolve("public/shardIcons").toFile();

        if (!Files.isDirectory(shardIcons.toPath()) || shardIcons.listFiles().length == 0) {
            LOGGER.info("Cloning skyshards repository to {}", images.getAbsolutePath());
            cloneRepoWithImages(images);
        }

        return Arrays.stream(shardIcons.listFiles()).filter(File::isFile).toArray(File[]::new);
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
    public File getShardData() {
        File dataDir = new File(REPO_DIR.resolve("data").toUri());
        File dataFile = new File(dataDir, "fusion-data.json");

        if (!Files.exists(dataFile.toPath())) {
            try (HttpClient client = HttpClient.newHttpClient()) {
                dataDir.mkdirs();
                dataFile.createNewFile();

                LOGGER.info("Downloading shard data to {}", dataFile.getAbsolutePath());

                HttpRequest request = HttpRequest.newBuilder(remoteDataPath).GET().build();
                HttpResponse<String> respone = client.send(request, HttpResponse.BodyHandlers.ofString());

                try (BufferedWriter writer = Files.newBufferedWriter(dataFile.toPath())) {
                    writer.write(respone.body());
                }

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
