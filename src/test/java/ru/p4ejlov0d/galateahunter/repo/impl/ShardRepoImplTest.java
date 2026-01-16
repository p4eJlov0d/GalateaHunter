package ru.p4ejlov0d.galateahunter.repo.impl;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.p4ejlov0d.galateahunter.utils.config.ModConfigHolder;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

class ShardRepoImplTest {
    @Test
    void getShardImages() {
        ModConfigHolder.register();
        File[] actual = ShardRepoImpl.getInstance().getShardImages();
        File images = new File(FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + "/images/assets/" + MOD_ID).toUri());

        assertTrue(images.exists() && actual.length > 0);
    }

    @Test
    void getShardData() {
        File expected = new File(FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + "/data/fusion-data.json").toUri());
        File data = ShardRepoImpl.getInstance().getShardData();

        assertEquals(expected.getAbsolutePath(), data.getAbsolutePath());
        assertTrue(data.length() > 0);
    }

    @AfterAll
    static void afterAll() {
        new File(FabricLoader.getInstance().getConfigDir().toString() + "/galateahunter.json5").delete();

        try {
            Field field = AutoConfig.class.getDeclaredField("holders");
            field.setAccessible(true);
            ((HashMap) field.get(null)).clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}