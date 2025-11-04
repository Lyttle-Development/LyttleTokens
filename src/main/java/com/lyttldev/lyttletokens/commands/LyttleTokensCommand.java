package com.lyttldev.lyttletokens.commands;

import com.lyttldev.lyttletokens.LyttleTokens;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public class LyttleTokensCommand {
    private static LyttleTokens plugin;

    public static void createCommand(LyttleTokens lyttlePlugin, Commands commands) {
        plugin = lyttlePlugin;

        // Define the different nodes
        LiteralArgumentBuilder<CommandSourceStack> top = Commands.literal("lyttletokens")
                .then(Commands.literal("reload")
                        .requires(source -> source.getSender().hasPermission("lyttletokens.lyttletokens.reload"))
                        .executes(LyttleTokensCommand::reloadNode));

        // Defines root node functions
        top.requires(source -> source.getSender().hasPermission("lyttletokens.lyttletokens"));
        top.executes(LyttleTokensCommand::rootNode);

        // Finish the command
        commands.register(
                top.build(),
                "Admin command for the LyttleTokens plugin"
        );
    }

    private static int rootNode(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        Component version = Component.text("Plugin version: " + plugin.getDescription().getVersion());
        sender.sendMessage(version);
        return Command.SINGLE_SUCCESS;
    }

    private static int reloadNode(CommandContext<CommandSourceStack> context) {
        final CommandSender sender = context.getSource().getSender();
        plugin.config.reload();
        sender.sendMessage(Component.text("The config has been reloaded"));
        return Command.SINGLE_SUCCESS;
    }
}
