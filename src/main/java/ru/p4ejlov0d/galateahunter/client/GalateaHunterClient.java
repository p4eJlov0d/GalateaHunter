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
    }
}
