package com.qclid.cohere.Shares;

import com.qclid.cohere.Cohere;
import com.qclid.cohere.Modules.Team.Teams;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Set;
import java.util.stream.Collectors;

public class PainShare implements Listener {

    private final Cohere plugin;

    public PainShare(Cohere plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!plugin.getConfig().getBoolean("coherences.painshare.enabled", true)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player damagedPlayer = (Player) event.getEntity();
        Teams teams = plugin.getTeams();

        if (!teams.canSharePain(damagedPlayer)) {
            return;
        }

        Set<Player> coherentPlayers = plugin.getCoherenceManager().getCoherentPlayers(damagedPlayer);
        Set<Player> coherentTeammates = coherentPlayers.stream()
                .filter(p -> teams.areInSameTeam(damagedPlayer, p))
                .collect(Collectors.toSet());

        if (coherentTeammates.isEmpty()) {
            return;
        }

        double originalDamage = event.getDamage();
        int totalPlayers = coherentTeammates.size() + 1;
        double sharedDamage = originalDamage / totalPlayers;

        event.setDamage(sharedDamage);

        for (Player teammate : coherentTeammates) {
            if (teammate.isDead() || teammate.getHealth() <= sharedDamage) {
                // Optional: prevent killing teammates with shared damage
                continue;
            }
            teammate.damage(sharedDamage);
            plugin.getDevLog().log("Shared " + sharedDamage + " damage from " + damagedPlayer.getName() + " to " + teammate.getName());
        }
        plugin.getDevLog().log("Damage for " + damagedPlayer.getName() + " reduced to " + sharedDamage);
    }
}
