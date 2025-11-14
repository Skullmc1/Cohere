package com.qclid.cohere.Modules;

import com.qclid.cohere.Cohere;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;

public class CoherenceManager {

    private final Cohere plugin;
    private final Map<Player, Set<Player>> coherenceMap = new HashMap<>();

    public CoherenceManager(Cohere plugin) {
        this.plugin = plugin;
    }

    public void updateCoherence() {
        double minDistance = plugin
            .getConfig()
            .getDouble("general-settings.min-distance", 10.0);

        Map<Player, Set<Player>> newCoherenceMap = new HashMap<>();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (plugin.getWorldManager().isWorldDisabled(player.getWorld())) {
                newCoherenceMap.put(player, new HashSet<>());
                continue;
            }
            Set<Player> newNearbyPlayers = player
                .getNearbyEntities(minDistance, minDistance, minDistance)
                .stream()
                .filter(
                    entity ->
                        entity instanceof Player &&
                        entity != player &&
                        !plugin
                            .getWorldManager()
                            .isWorldDisabled(((Player) entity).getWorld())
                )
                .map(entity -> (Player) entity)
                .collect(Collectors.toSet());
            newCoherenceMap.put(player, newNearbyPlayers);
        }

        Set<Set<Player>> processedEnterPairs = new HashSet<>();
        Set<Set<Player>> processedLeavePairs = new HashSet<>();

        for (Player player : newCoherenceMap.keySet()) {
            Set<Player> oldNearby = this.coherenceMap.getOrDefault(
                player,
                new HashSet<>()
            );
            Set<Player> newNearby = newCoherenceMap.get(player);

            // Find newly entered players
            for (Player nearbyPlayer : newNearby) {
                if (!oldNearby.contains(nearbyPlayer)) {
                    Set<Player> pair = new HashSet<>();
                    pair.add(player);
                    pair.add(nearbyPlayer);
                    if (processedEnterPairs.add(pair)) {
                        plugin
                            .getGeneralMessages()
                            .sendCoherenceMessage(
                                nearbyPlayer,
                                player.getName()
                            );
                        plugin
                            .getGeneralMessages()
                            .sendCoherenceMessage(
                                player,
                                nearbyPlayer.getName()
                            );
                    }
                }
            }

            // Find players who left
            for (Player nearbyPlayer : oldNearby) {
                if (!newNearby.contains(nearbyPlayer)) {
                    Set<Player> pair = new HashSet<>();
                    pair.add(player);
                    pair.add(nearbyPlayer);
                    if (processedLeavePairs.add(pair)) {
                        plugin
                            .getGeneralMessages()
                            .sendLeaveCoherenceMessage(
                                nearbyPlayer,
                                player.getName()
                            );
                        plugin
                            .getGeneralMessages()
                            .sendLeaveCoherenceMessage(
                                player,
                                nearbyPlayer.getName()
                            );
                    }
                }
            }
        }

        this.coherenceMap.clear();
        this.coherenceMap.putAll(newCoherenceMap);
    }

    public Set<Player> getCoherentPlayers(Player player) {
        return coherenceMap.getOrDefault(player, new HashSet<>());
    }

    public void removePlayer(Player player) {
        coherenceMap.remove(player);
        coherenceMap.forEach((p, players) -> players.remove(player));
    }
}
