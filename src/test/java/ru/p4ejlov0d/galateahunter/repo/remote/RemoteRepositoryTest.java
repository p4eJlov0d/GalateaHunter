package ru.p4ejlov0d.galateahunter.repo.remote;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.p4ejlov0d.galateahunter.utils.config.ModConfigHolder;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RemoteRepositoryTest {
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
    void register() {
        ModConfigHolder.register();
        RemoteRepository.getInstance().cloneRepoWithImages();

        ModConfigHolder.getConfig().setImagesCount(0);
        ModConfigHolder.save();

        CompletableFuture<Void> f = RemoteRepository.getInstance().register().thenRun(
                () -> assertTrue(RemoteRepository.getInstance().isNeedUpdate())
        );

        CompletableFuture.allOf(f).join();
    }
}