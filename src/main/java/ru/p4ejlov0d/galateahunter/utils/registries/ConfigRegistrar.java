package ru.p4ejlov0d.galateahunter.utils.registries;

import ru.p4ejlov0d.galateahunter.config.GalateaHunterConfig;

public class ConfigRegistrar implements GalateaHunterModRegistrar {

    @Override
    public void register() {
        new GalateaHunterConfig();
    }
}
