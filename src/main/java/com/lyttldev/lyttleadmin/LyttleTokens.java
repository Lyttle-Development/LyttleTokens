package com.lyttldev.lyttletokens;

import com.lyttldev.lyttletokens.commands.*;
import com.lyttldev.lyttletokens.database.SQLite;
import com.lyttldev.lyttletokens.handlers.PlayerJoinListener;
import com.lyttldev.lyttletokens.types.Configs;
import com.lyttldev.lyttletokens.utils.Console;
import com.lyttldev.lyttletokens.utils.Message;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class LyttleTokens extends JavaPlugin {
    public Configs config;
    private Economy economy;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        // Setup config after creating the configs
        config = new Configs(this);
        // Migrate config
        migrateConfig();

        if (!setupEconomy()) {
            getLogger().severe("Vault or an economy plugin is not installed!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Plugin startup logic
        Console.init(this);
        Message.init(this);

        // Commands
        new LyttleTokensCommand(this);

        // Listeners
        new PlayerJoinListener(this);
    }

    @Override
    public void saveDefaultConfig() {
        String configPath = "config.yml";
        if (!new File(getDataFolder(), configPath).exists())
            saveResource(configPath, false);

        String messagesPath = "messages.yml";
        if (!new File(getDataFolder(), messagesPath).exists())
            saveResource(messagesPath, false);

        // Defaults:
        String defaultPath = "#defaults/";
        String defaultGeneralPath =  defaultPath + configPath;
        saveResource(defaultGeneralPath, true);

        String defaultMessagesPath =  defaultPath + messagesPath;
        saveResource(defaultMessagesPath, true);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }

    private void migrateConfig() {
        if (!config.general.contains("config_version")) {
            config.general.set("config_version", 0);
        }

        switch (config.general.get("config_version").toString()) {
            case "0":
                // Migrate config entries.
                config.messages.set("prefix", config.defaultMessages.get("prefix"));
                config.messages.set("no_permission", config.defaultMessages.get("no_permission"));
                config.messages.set("player_not_found", config.defaultMessages.get("player_not_found"));
                config.messages.set("must_be_player", config.defaultMessages.get("must_be_player"));
                config.messages.set("message_not_found", config.defaultMessages.get("message_not_found"));
                config.general.set("scoreboard_name", config.defaultGeneral.get("scoreboard_name"));

                // Update config version.
                config.general.set("config_version", 1);

                // Recheck if the config is fully migrated.
                migrateConfig();
                break;
            default:
                break;
        }
    }
}
