package com.lyttldev.lyttletokens.commands;

import com.lyttldev.lyttletokens.LyttleTokens;
import com.lyttldev.lyttletokens.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class LyttleTokensCommand implements CommandExecutor, TabCompleter {
    private final LyttleTokens plugin;

    public LyttleTokensCommand(LyttleTokens plugin) {
        plugin.getCommand("lyttletokens").setExecutor(this);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for permission
        if (!(sender.hasPermission("lyttletokens.lyttletokens"))) {
            Message.sendMessage(sender, "no_permission");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("plugin version: 1.1.2");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.config.reload();
                Message.sendMessageRaw(sender, "The config has been reloaded");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] arguments) {
        if (arguments.length == 1) {
            return List.of("reload");
        }
        return List.of();
    }
}
