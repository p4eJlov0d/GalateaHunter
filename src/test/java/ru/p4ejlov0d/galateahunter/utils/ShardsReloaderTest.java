package ru.p4ejlov0d.galateahunter.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShardsReloaderTest {
    @Test
    void load() {
        ShardsReloader<Map<String, Pair<String, File>>> shardsReloader = new ShardsReloader<>();

        shardsReloader.load(null, new ForkJoinPool(15)).thenAccept(map -> map.forEach((key, value) -> {
            String expectedId = key.substring(1).split("\"")[0];
            String actualId = value.getLeft();
            File expectedImage = value.getRight();

            assertEquals(expectedId, actualId);
            assertTrue(expectedImage.exists());
        }));
    }

    @Test
    void apply() {
    }
}