package com.qclid.cohere.Shares;

import com.qclid.cohere.Cohere;
import com.qclid.cohere.Modules.Team.Teams;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionShare {

    private final Cohere plugin;
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

        Teams teams = plugin.getTeams();
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
                if (isEffectShared(playerA, effectA.getType())) continue;

                for (Player playerB : coherentPlayers) {
                    if (
                        !teams.areInSameTeam(playerA, playerB) ||
                        !teams.canSharePotions(playerA)
                    ) {
                        continue;
                    }

                    PotionEffect effectB = playerB.getPotionEffect(
                        effectA.getType()
                    );

                    if (
                        effectB == null ||
                        effectA.getAmplifier() > effectB.getAmplifier()
                    ) {
                        int sharedAmplifier = Math.max(
                            0,
                            effectA.getAmplifier() - levelDecrease
                        );

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

        sharedEffectsTracker.remove(player);
    }
}
