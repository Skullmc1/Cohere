package com.qclid.cohere.Shares;

import com.qclid.cohere.Cohere;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class XPShare implements Listener {

    private final Cohere plugin;

    public XPShare(Cohere plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (
            !plugin.getConfig().getBoolean("coherences.xpshare.enabled", true)
        ) {
            return;
        }
        Player player = event.getPlayer();
        int amount = event.getAmount();

        if (amount <= 0) {
            return;
        }

        String shareType = plugin
            .getConfig()
            .getString("coherences.xpshare.type", "add");

        Set<Player> nearbyPlayers = plugin
            .getCoherenceManager()
            .getCoherentPlayers(player);

        if (nearbyPlayers.isEmpty()) {
            return;
        }

        if ("subtract".equalsIgnoreCase(shareType)) {
            int totalPlayers = nearbyPlayers.size() + 1;
            int sharedAmount = amount / totalPlayers;
            int remainder = amount % totalPlayers;

            event.setAmount(sharedAmount + remainder);

            for (Player nearbyPlayer : nearbyPlayers) {
                nearbyPlayer.giveExp(sharedAmount);
                plugin
                    .getGeneralMessages()
                    .sendXPShareMessage(nearbyPlayer, "XP Shared!");
                plugin
                    .getDevLog()
                    .log(
                        "Shared " +
                            sharedAmount +
                            " XP from " +
                            player.getName() +
                            " to " +
                            nearbyPlayer.getName() +
                            " (subtract)"
                    );
            }
        } else {
            // 'add' type
            double sharedPercentage = plugin
                .getConfig()
                .getDouble("coherences.xpshare.shared-percentage", 50.0);
            int sharedAmount = (int) (amount * (sharedPercentage / 100.0));

            if (sharedAmount > 0) {
                for (Player nearbyPlayer : nearbyPlayers) {
                    nearbyPlayer.giveExp(sharedAmount);
                    plugin
                        .getGeneralMessages()
                        .sendXPShareMessage(
                            nearbyPlayer,
                            "+" + sharedAmount + " XP"
                        );
                    plugin
                        .getDevLog()
                        .log(
                            "Shared " +
                                sharedAmount +
                                " XP from " +
                                player.getName() +
                                " to " +
                                nearbyPlayer.getName() +
                                " (add)"
                        );
                }
            }
        }
    }
}
