package com.qclid.cohere.Shares;

import com.qclid.cohere.Cohere;
import com.qclid.cohere.Modules.Team.Teams;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeathDebuffs implements Listener {

    private final Cohere plugin;

    public DeathDebuffs(Cohere plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!plugin.getConfig().getBoolean("coherences.deathdebuffs.enabled", true)) {
            return;
        }

        Player deadPlayer = event.getEntity();
        Teams teams = plugin.getTeams();
        Set<Player> coherentPlayers = plugin.getCoherenceManager().getCoherentPlayers(deadPlayer);

        for (Player coherentPlayer : coherentPlayers) {
            if (teams.areInSameTeam(deadPlayer, coherentPlayer) && teams.canReceiveDeathDebuffs(coherentPlayer)) {
                applyDebuffs(coherentPlayer);
            }
        }
    }

    private void applyDebuffs(Player player) {
        List<Map<?, ?>> effects = plugin.getConfig().getMapList("coherences.deathdebuffs.effects");
        for (Map<?, ?> effectMap : effects) {
            try {
                String typeStr = (String) effectMap.get("type");
                PotionEffectType type = PotionEffectType.getByName(typeStr);
                if (type == null) {
                    plugin.getLogger().warning("[DeathDebuffs] Invalid effect type: " + typeStr);
                    continue;
                }

                int duration = (int) effectMap.get("duration") * 20; // Convert seconds to ticks
                int amplifier = (int) effectMap.get("amplifier");

                player.addPotionEffect(new PotionEffect(type, duration, amplifier));
                plugin.getDevLog().log("Applied " + type.getName() + " to " + player.getName() + " due to teammate death.");

            } catch (Exception e) {
                plugin.getLogger().severe("[DeathDebuffs] Failed to parse effect from config.yml.");
                e.printStackTrace();
            }
        }
    }
}
