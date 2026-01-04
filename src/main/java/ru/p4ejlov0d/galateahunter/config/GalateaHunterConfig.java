package ru.p4ejlov0d.galateahunter.config;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GalateaHunterConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(GalateaHunterConfig.class);
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir();
    private static final File CONFIG_FILE = CONFIG_PATH.resolve("galateahunter.json").toFile();

    public GalateaHunterConfig() {
        try {
            createConfig();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            System.exit(-1);
        }
    }

    private static void createConfig() throws IOException {
        if (!CONFIG_FILE.exists()) {
            CONFIG_FILE.getParentFile().mkdirs();
            Files.createFile(CONFIG_FILE.toPath());
        }
    }
}
