package ru.p4ejlov0d.galateahunter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.p4ejlov0d.galateahunter.utils.config.ModConfigHolder;
import ru.p4ejlov0d.galateahunter.utils.registries.GalateaHunterModRegistrar;

import java.lang.reflect.Method;

public class GalateaHunter implements ClientModInitializer {

    public static final String MOD_ID = "galateahunter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow();
    public static final String VERSION = MOD_CONTAINER.getMetadata().getVersion().getFriendlyString();
    public static final String NAME = MOD_CONTAINER.getMetadata().getName();

    @Override
    public void onInitializeClient() {
        LOGGER.info("[{}] Initializing", NAME);

        ModConfigHolder.register();

        for (Class<?> registrar : new Reflections(GalateaHunterModRegistrar.class).getSubTypesOf(GalateaHunterModRegistrar.class)) {
            String className = registrar.getSimpleName();
            try {
                LOGGER.debug("Start registering {}", className);

                Method register = registrar.getMethod("register");
                register.invoke(registrar.getDeclaredConstructor().newInstance());

                LOGGER.debug("Successfully finished registering {}", className);
            } catch (Exception e) {
                LOGGER.error("Failed to register {}", className, e);
                System.exit(-1);
            }
        }

        LOGGER.info("[{}] Successfully initialized", NAME);
    }
}
