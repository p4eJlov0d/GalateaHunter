package ru.p4ejlov0d.galateahunter.utils;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.Identifier;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

public class ShardsReloader implements SimpleSynchronousResourceReloadListener {

    /**
     * @return The unique identifier of this listener.
     */
    @Override
    public Identifier getFabricId() {
        return Identifier.tryParse(MOD_ID, "load_shard_data");
    }

    /**
     * Performs the reload in the apply executor, or the game engine.
     *
     * @param manager the resource manager
     */
    @Override
    public void reload(ResourceManager manager) {
        try (ResourcePack resourcePack = manager.streamResourcePacks()
                .filter(resourcePack1 -> resourcePack1.getId().equals(MOD_ID))
                .findFirst().orElse(null)
        ) {
            ShardManager.getInstance().setResourcePack(resourcePack);
            ShardManager.getInstance().load();
        }
    }
}
