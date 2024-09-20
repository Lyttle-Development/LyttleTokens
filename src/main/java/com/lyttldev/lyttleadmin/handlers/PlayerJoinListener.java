package com.lyttldev.lyttletokens.handlers;

import com.lyttldev.lyttletokens.LyttleTokens;
import com.lyttldev.lyttletokens.commands.StaffCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    public PlayerJoinListener(LyttleTokens plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        StaffCommand.onPlayerJoin(event.getPlayer());
    }
}
