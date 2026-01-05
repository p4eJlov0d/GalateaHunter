package ru.p4ejlov0d.galateahunter.utils.registries;

import ru.p4ejlov0d.galateahunter.command.MainGuiCommand;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.LOGGER;

public class CommandsRegistrar implements GalateaHunterModRegistrar {
    @Override
    public void register() {
        LOGGER.debug("Registering main gui command");
        MainGuiCommand.register();
        LOGGER.debug("Main gui command has been registered");
    }
}
