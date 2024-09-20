package com.lyttldev.lyttletokens.types;

import com.lyttldev.lyttletokens.LyttleTokens;

public class Configs {
    private final LyttleTokens plugin;

    // Configs
    public Config general;
    public Config messages;

    // Default configs
    public Config defaultGeneral;
    public Config defaultMessages;


    public Configs(LyttleTokens plugin) {
        this.plugin = plugin;

        // Configs
        general = new Config(plugin, "config.yml");
        messages = new Config(plugin, "messages.yml");

        // Default configs
        defaultMessages = new Config(plugin, "#defaults/messages.yml");
        defaultGeneral = new Config(plugin, "#defaults/config.yml");
    }

    public void reload() {
        general.reload();
        messages.reload();

        plugin.reloadConfig();
    }

    private String getConfigPath(String path) {
        return plugin.getConfig().getString("configs." + path);
    }
}
