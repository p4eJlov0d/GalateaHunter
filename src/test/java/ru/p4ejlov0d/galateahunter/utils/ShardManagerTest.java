package ru.p4ejlov0d.galateahunter.utils;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.impl.resource.loader.ModNioResourcePack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.p4ejlov0d.galateahunter.repo.impl.ShardRepoImpl;
import ru.p4ejlov0d.galateahunter.utils.config.ModConfigHolder;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

class ShardManagerTest {

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

    @Test
    void load() {
        ModConfigHolder.register();

        ShardManager.getInstance().setResourcePack(ModNioResourcePack.create(MOD_ID, FabricLoader.getInstance().getModContainer(MOD_ID).get(), null, ResourceType.CLIENT_RESOURCES, null, false));
        CompletableFuture<Void> f = ShardManager.getInstance().load().thenRun(() -> {
            Path configDir = FabricLoader.getInstance().getConfigDir();
            File images = new File(configDir.resolve(MOD_ID + "/images/assets/" + MOD_ID).toUri());
            File dataFile = new File(configDir.resolve(MOD_ID + "/data/fusion-data.json").toUri());

            assertTrue(images.exists());
            assertTrue(dataFile.exists());
            assertEquals(ShardRepoImpl.getInstance().getShards().size(), images.listFiles().length);
        });

        CompletableFuture.allOf(f).join();
    }
}