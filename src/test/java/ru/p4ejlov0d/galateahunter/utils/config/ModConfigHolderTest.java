package ru.p4ejlov0d.galateahunter.utils.config;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.p4ejlov0d.galateahunter.config.GalateaHunterConfig;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ModConfigHolderTest {
    @Test
    void register() {
        ModConfigHolder.register();
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toString() + "/galateahunter.json5");

        assertTrue(configFile.length() > 0);
    }

    @Test
    void getConfig() {
        assertDoesNotThrow(ModConfigHolder::getConfig);
    }

    @Test
    void save() {
        GalateaHunterConfig expected = new GalateaHunterConfig();
        expected.setLanguageCode("aaa");

        ModConfigHolder.getConfig().setLanguageCode("aaa");
        ModConfigHolder.save();
        ModConfigHolder.getConfig().setLanguageCode("bbb");
        AutoConfig.getConfigHolder(GalateaHunterConfig.class).load();

        assertEquals(expected.getLanguageCode(), ModConfigHolder.getConfig().getLanguageCode());
    }

    @Test
    void reset() {
        ModConfigHolder.getConfig().setLanguageCode("ccc");
        ModConfigHolder.reset();

        assertEquals(new GalateaHunterConfig().getLanguageCode(), ModConfigHolder.getConfig().getLanguageCode());
    }

    @AfterEach
    void afterEach() {
        new File(FabricLoader.getInstance().getConfigDir().toString() + "/galateahunter.json5").delete();
    }
}