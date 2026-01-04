package ru.p4ejlov0d.galateahunter.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import ru.p4ejlov0d.galateahunter.client.GalateaHunterScreen;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            return new GalateaHunterScreen();
        };
    }
}
