package com.qclid.cohere.Shares;

import com.qclid.cohere.Cohere;
import com.qclid.cohere.Modules.Team.Teams;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PainShare implements Listener {

    private final Cohere plugin;
    private final Set<Player> damaging = new HashSet<>();

    public PainShare(Cohere plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (
            !plugin.getConfig().getBoolean("coherences.painshare.enabled", true)
        ) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player damagedPlayer = (Player) event.getEntity();
        if (damaging.contains(damagedPlayer)) {
            return;
        }

        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageByEntityEvent =
                (EntityDamageByEntityEvent) event;
            if (damageByEntityEvent.getDamager() instanceof Player) {
                Player damager = (Player) damageByEntityEvent.getDamager();
                if (damager.equals(damagedPlayer)) {
                    return;
                }
            }
        }

        Teams teams = plugin.getTeams();

        if (!teams.canSharePain(damagedPlayer)) {
            return;
        }

        Set<Player> coherentPlayers = plugin
            .getCoherenceManager()
            .getCoherentPlayers(damagedPlayer);
        Set<Player> coherentTeammates = coherentPlayers
            .stream()
            .filter(p -> teams.areInSameTeam(damagedPlayer, p))
            .collect(Collectors.toSet());

        if (coherentTeammates.isEmpty()) {
            return;
        }

        double originalDamage = event.getDamage();
        int totalPlayers = coherentTeammates.size() + 1;
        double sharedDamage = originalDamage / totalPlayers;

        event.setDamage(sharedDamage);

        damaging.add(damagedPlayer);
        for (Player teammate : coherentTeammates) {
            if (teammate.isDead()) {
                continue;
            }

            double cappedDamage = Math.min(
                sharedDamage,
                teammate.getHealth() * 0.9
            );

            damaging.add(teammate);
            teammate.damage(cappedDamage);
            damaging.remove(teammate);
            plugin
                .getDevLog()
                .log(
                    "Shared " +
                        cappedDamage +
                        " damage from " +
                        damagedPlayer.getName() +
                        " to " +
                        teammate.getName()
                );
        }
        damaging.remove(damagedPlayer);
        plugin
            .getDevLog()
            .log(
                "Damage for " +
                    damagedPlayer.getName() +
                    " reduced to " +
                    sharedDamage
            );
    }
}
