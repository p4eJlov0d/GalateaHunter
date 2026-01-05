package ru.p4ejlov0d.galateahunter.command;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import ru.p4ejlov0d.galateahunter.client.GalateaHunterScreen;

public class MainGuiCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess)
                -> dispatcher.register(ClientCommandManager.literal("gh").executes(MainGuiCommand::execute))
        );
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        MinecraftClient client = context.getSource().getClient();
        client.send(() -> client.setScreen(GalateaHunterScreen.createGui()));

        return 1;
    }
}
