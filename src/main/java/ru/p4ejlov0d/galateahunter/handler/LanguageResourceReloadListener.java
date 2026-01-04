package ru.p4ejlov0d.galateahunter.handler;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import ru.p4ejlov0d.galateahunter.client.GalateaHunterScreen;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.LOGGER;
import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

public class LanguageResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return Identifier.tryParse(MOD_ID, "load_lang");
    }

    @Override
    public void reload(ResourceManager manager) {
        LOGGER.debug("Starting to load language resources");
        for (Identifier id : manager.findResources("lang", path -> path.toString().endsWith(".json")).keySet()) {
            if (id.getNamespace().equals(MOD_ID)) {
                GalateaHunterScreen.LANG_FILES.put(id.getPath().split("/")[1].split("\\.")[0], manager.getResource(id).get());
                LOGGER.debug("Loaded language resource: " + id);
            }
        }
        LOGGER.debug("Finished loading language resources with " + GalateaHunterScreen.LANG_FILES.size() + " loaded resources");
    }
}
