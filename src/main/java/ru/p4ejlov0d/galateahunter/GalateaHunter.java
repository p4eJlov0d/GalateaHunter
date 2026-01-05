package ru.p4ejlov0d.galateahunter;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GalateaHunter implements ModInitializer {
    public static final String MOD_ID = "galateahunter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow();
    public static final String VERSION = MOD_CONTAINER.getMetadata().getVersion().getFriendlyString();
    public static final String NAME = MOD_CONTAINER.getMetadata().getName();

    @Override
    public void onInitialize() {
        LOGGER.info("[{}] Initialize", NAME);
    }
}
