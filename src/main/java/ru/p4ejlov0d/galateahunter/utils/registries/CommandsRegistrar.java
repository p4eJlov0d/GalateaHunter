package ru.p4ejlov0d.galateahunter.utils.registries;

import ru.p4ejlov0d.galateahunter.command.MainGuiCommand;

public class CommandsRegistrar implements GalateaHunterModRegistrar {
    @Override
    public void register() {
        MainGuiCommand.register();
    }
}
