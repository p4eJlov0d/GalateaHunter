package ru.p4ejlov0d.galateahunter.repo.impl;

import net.fabricmc.loader.api.FabricLoader;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

class ShardRepoImplTest {
    @Test
    void getShardImages() {
        File[] actual = ShardRepoImpl.getInstance().getShardImages();
        File images = new File(FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + "/repo/images/public/shardicons").toUri());

        assertTrue(images.exists());
        assertEquals(181, actual.length);
    }

    @Test
    void getShardData() {
        File expected = new File(FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + "/repo/data/fusion-data.json").toUri());
        File data = ShardRepoImpl.getInstance().getShardData();

        assertEquals(expected.getAbsolutePath(), data.getAbsolutePath());
        assertTrue(data.length() > 0);
    }
}