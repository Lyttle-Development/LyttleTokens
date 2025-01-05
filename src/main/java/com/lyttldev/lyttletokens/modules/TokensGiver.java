package com.lyttldev.lyttletokens.modules;

import com.lyttldev.lyttletokens.LyttleTokens;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;

import static com.lyttldev.lyttletokens.utils.Message.getMessage;

public class TokensGiver {
    private static Integer tokensInterval = 111;
    private static Integer tokensAmount = 1;
    private static String afkObjectiveName = "afk-time";
    private static Integer afkTime = 600;
    private static Scoreboard scoreboard;

    public static void init(LyttleTokens plugin, Economy economy) {
        // Set the values from the config
        TokensGiver.afkObjectiveName = (String) plugin.config.general.get("afk_scoreboard_name");
        TokensGiver.afkTime = (Integer) plugin.config.general.get("afk_time");
        TokensGiver.tokensInterval = (Integer) plugin.config.general.get("tokens_time");
        TokensGiver.tokensAmount = (Integer) plugin.config.general.get("tokens_amount");

        TokensGiver.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        // Create a loop that gives tokens to players every 111 seconds.
        Bukkit.getScheduler().runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("LyttleTokens")), () -> {
            Objective objective = scoreboard.getObjective(afkObjectiveName);
            if (objective == null) {
                // Throw an error if the objective is not found
                throw new NullPointerException("Objective " + afkObjectiveName + " not found in the scoreboard!");
            }

            // Give tokens to all online players
            Bukkit.getOnlinePlayers().forEach(player -> {
                // Check if the player is AFK
                int score = objective.getScore(player.getName()).getScore();
                if (score > afkTime) {
                    return;
                }

                economy.depositPlayer(player, tokensAmount);

                // Send scoreboard action bar message
                String[][] replacements = {
                    {"<AMOUNT>", "1"},
                };
                Component message = getMessage("tokens_received", replacements);
                player.sendActionBar(message);

                // Wait 2 seconds before sending the 2nd message
                Bukkit.getScheduler().runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("LyttleTokens")), () -> {
                    double tokens = economy.getBalance(player);
                    int totalTokens = (int) tokens;
                    // Send scoreboard action bar message
                    String[][] replacements2 = {
                        {"<AMOUNT>", Integer.toString(totalTokens)},
                    };
                    Component message2 = getMessage("tokens_received_total", replacements2);
                    player.sendActionBar(message2);
                }, 2 * 20);
            });
        }, 0, tokensInterval * 20);
    }
}
