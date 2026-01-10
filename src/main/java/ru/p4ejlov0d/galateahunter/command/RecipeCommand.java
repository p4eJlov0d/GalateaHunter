package ru.p4ejlov0d.galateahunter.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import ru.p4ejlov0d.galateahunter.screen.RecipeScreen;

public class RecipeCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess)
                -> dispatcher.register(ClientCommandManager.literal("ghrecipe").executes(RecipeCommand::openEmptyRecipeScreen)
                .then(ClientCommandManager.argument("recipe", StringArgumentType.greedyString()).executes(RecipeCommand::openFilledRecipeScreen))
        ));
    }

    private static int openEmptyRecipeScreen(CommandContext<FabricClientCommandSource> context) {
        MinecraftClient client = context.getSource().getClient();
        client.send(() -> client.setScreen(new RecipeScreen()));

        return 1;
    }

    private static int openFilledRecipeScreen(CommandContext<FabricClientCommandSource> context) {
        MinecraftClient client = context.getSource().getClient();
        client.send(() -> client.setScreen(new RecipeScreen(StringArgumentType.getString(context, "recipe"))));

        return 1;
    }
}
