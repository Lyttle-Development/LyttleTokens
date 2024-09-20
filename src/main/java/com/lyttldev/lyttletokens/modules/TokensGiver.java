package com.lyttldev.lyttletokens.modules;

import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;

import java.util.Objects;

import static com.lyttldev.lyttletokens.utils.Message.getMessage;

public class TokensGiver {
    static Economy economy;

    public TokensGiver(Economy economy) {
        TokensGiver.economy = economy;

        // Create a loop that gives tokens to players every 111 seconds.
        Bukkit.getScheduler().runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("LyttleTokens")), () -> {
            // Give tokens to all online players
            Bukkit.getOnlinePlayers().forEach(player -> {
                economy.depositPlayer(player, 1);

                // Send scoreboard action bar message
                String[][] replacements = {
                        {"<AMOUNT>", "1"},
                };
                Component message = getMessage("tokens_received", replacements);
                player.sendActionBar(message);
            });
        }, 0, 111 * 20);
    }
}
