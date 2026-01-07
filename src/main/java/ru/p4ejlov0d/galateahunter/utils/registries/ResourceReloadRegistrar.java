package ru.p4ejlov0d.galateahunter.utils.registries;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import ru.p4ejlov0d.galateahunter.utils.LanguageResourceHandler;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.LOGGER;

public class ResourceReloadRegistrar implements GalateaHunterModRegistrar {
    @Override
    public void register() {
        LOGGER.debug("Registering language resource handler");
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(LanguageResourceHandler.getInstance());
        LOGGER.debug("Language resource handler has been registered");
    }
}
