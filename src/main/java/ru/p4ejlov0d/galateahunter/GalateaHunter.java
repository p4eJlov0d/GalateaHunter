package ru.p4ejlov0d.galateahunter;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GalateaHunter implements ModInitializer {
    public static final String MOD_ID = "galateahunter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("[GalateaHunter] Initialize");
    }
}
