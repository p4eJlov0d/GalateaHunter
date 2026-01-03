package ru.p4ejlov0d.galateahunter.client;

import net.fabricmc.api.ClientModInitializer;
import ru.p4ejlov0d.galateahunter.utils.CommandsRegistry;

public class GalateaHunterClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CommandsRegistry.register();
    }
}
