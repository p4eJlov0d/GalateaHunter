package ru.p4ejlov0d.galateahunter.utils.registries;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import ru.p4ejlov0d.galateahunter.utils.LanguageResourceHandler;
import ru.p4ejlov0d.galateahunter.utils.ShardsReloader;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.LOGGER;

public class ResourceReloadRegistrar implements GalateaHunterModRegistrar {
    public static void lazyShardsReloaderRegister() {
        LOGGER.debug("Registering shard data resources");
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ShardsReloader<>());
        LOGGER.debug("Shard data resources has been registered");
    }

    @Override
    public void register() {
        LOGGER.debug("Registering language resource handler");
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(LanguageResourceHandler.getInstance());
        LOGGER.debug("Language resource handler has been registered");
    }
}
