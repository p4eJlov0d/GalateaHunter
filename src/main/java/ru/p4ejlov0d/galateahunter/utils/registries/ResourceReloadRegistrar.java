package ru.p4ejlov0d.galateahunter.utils.registries;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import ru.p4ejlov0d.galateahunter.handler.LanguageResourceReloadListener;

public class ResourceReloadRegistrar implements GalateaHunterModRegistrar {
    @Override
    public void register() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new LanguageResourceReloadListener());
    }
}
