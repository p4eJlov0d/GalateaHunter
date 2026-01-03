package ru.p4ejlov0d.galateahunter.client;

import net.fabricmc.api.ClientModInitializer;
import org.reflections.Reflections;
import ru.p4ejlov0d.galateahunter.utils.registries.GalateaHunterModRegistrar;

import java.lang.reflect.Method;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.LOGGER;

public class GalateaHunterClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (Class<?> registrar : new Reflections(GalateaHunterModRegistrar.class).getSubTypesOf(GalateaHunterModRegistrar.class)) {
            try {
                LOGGER.info("Start registering " + registrar.getSimpleName());
                Method register = registrar.getMethod("register", null);
                register.invoke(registrar.getDeclaredConstructor().newInstance(), null);
                LOGGER.info("Successfully finished registering " + registrar.getSimpleName());
            } catch (Exception e) {
                LOGGER.error("Failed to register " + registrar.getSimpleName() + ", caused by " + e.getMessage());
                System.exit(-1);
            }
        }
    }
}
