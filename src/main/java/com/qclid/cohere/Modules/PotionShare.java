package com.qclid.cohere.Modules;

import com.qclid.cohere.Cohere;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionShare {

    private final Cohere plugin;
    // Tracks which player received which effect from whom
    // Recipient -> {EffectType -> Giver}
    private final Map<
        Player,
        Map<PotionEffectType, Player>
    > sharedEffectsTracker = new HashMap<>();

    public PotionShare(Cohere plugin) {
        this.plugin = plugin;
    }

    public void update() {
        if (
            !plugin
                .getConfig()
                .getBoolean("coherences.potionshare.enabled", true)
        ) {
            return;
        }

        int levelDecrease = plugin
            .getConfig()
            .getInt("coherences.potionshare.level-decrease", 1);
        Map<Player, Map<PotionEffectType, Player>> newlySharedEffects =
            new HashMap<>();

        for (Player playerA : plugin.getServer().getOnlinePlayers()) {
            if (
                plugin.getWorldManager().isWorldDisabled(playerA.getWorld())
            ) continue;

            Set<Player> coherentPlayers = plugin
                .getCoherenceManager()
                .getCoherentPlayers(playerA);
            if (coherentPlayers.isEmpty()) continue;

            for (PotionEffect effectA : playerA.getActivePotionEffects()) {
                // Skip if the effect on playerA is a shared one, to prevent loops
                if (isEffectShared(playerA, effectA.getType())) continue;

                for (Player playerB : coherentPlayers) {
                    PotionEffect effectB = playerB.getPotionEffect(
                        effectA.getType()
                    );

                    // Share if playerB doesn't have the effect, or playerA's is stronger
                    if (
                        effectB == null ||
                        effectA.getAmplifier() > effectB.getAmplifier()
                    ) {
                        int sharedAmplifier = Math.max(
                            0,
                            effectA.getAmplifier() - levelDecrease
                        );

                        // Apply the shared effect for 5 seconds (100 ticks)
                        PotionEffect sharedEffect = new PotionEffect(
                            effectA.getType(),
                            100,
                            sharedAmplifier,
                            true,
                            false
                        );
                        playerB.addPotionEffect(sharedEffect);

                        newlySharedEffects
                            .computeIfAbsent(playerB, k -> new HashMap<>())
                            .put(effectA.getType(), playerA);
                        plugin
                            .getDevLog()
                            .log(
                                "Shared " +
                                    effectA.getType().getName() +
                                    " from " +
                                    playerA.getName() +
                                    " to " +
                                    playerB.getName()
                            );
                    }
                }
            }
        }

        // Clean up effects that are no longer shared
        sharedEffectsTracker.forEach((recipient, effects) -> {
            effects.forEach((type, giver) -> {
                if (
                    !newlySharedEffects
                        .getOrDefault(recipient, new HashMap<>())
                        .containsKey(type)
                ) {
                    recipient.removePotionEffect(type);
                    plugin
                        .getDevLog()
                        .log(
                            "Removed shared " +
                                type.getName() +
                                " from " +
                                recipient.getName()
                        );
                }
            });
        });

        sharedEffectsTracker.clear();
        sharedEffectsTracker.putAll(newlySharedEffects);
    }

    private boolean isEffectShared(Player player, PotionEffectType type) {
        return sharedEffectsTracker
            .getOrDefault(player, new HashMap<>())
            .containsKey(type);
    }

    public void cleanUpPlayer(Player player) {
        // Remove effects that were GIVEN BY the quitting player
        sharedEffectsTracker.forEach((recipient, effects) -> {
            Set<PotionEffectType> toRemove = new HashSet<>();
            effects.forEach((type, giver) -> {
                if (giver.equals(player)) {
                    toRemove.add(type);
                    recipient.removePotionEffect(type);
                }
            });
            toRemove.forEach(effects::remove);
        });

        // Remove effects that were RECEIVED BY the quitting player
        sharedEffectsTracker.remove(player);
    }
}
