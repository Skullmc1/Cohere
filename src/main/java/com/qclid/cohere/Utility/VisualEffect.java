package com.qclid.cohere.Utility;

import com.qclid.cohere.Modules.CoherenceManager;
import com.qclid.cohere.Modules.Team.Teams;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class VisualEffect {

    private final CoherenceManager coherenceManager;
    private final Teams teams;

    public VisualEffect(CoherenceManager coherenceManager, Teams teams) {
        this.coherenceManager = coherenceManager;
        this.teams = teams;
    }

    public void displayEffect(Player effectPlayer) {
        if (!coherenceManager.getCoherentPlayers(effectPlayer).isEmpty()) {
            String particleName = teams.getTeamParticle(effectPlayer);
            Particle particle;
            try {
                particle = Particle.valueOf(particleName.toUpperCase());
            } catch (IllegalArgumentException e) {
                particle = Particle.VAULT_CONNECTION; // Fallback to default
            }

            for (Player viewer : Bukkit.getOnlinePlayers()) {
                if (!viewer.equals(effectPlayer)) {
                    viewer.spawnParticle(
                        particle,
                        effectPlayer.getEyeLocation().add(0, 0.8, 0),
                        10,
                        0.1,
                        0.1,
                        0.1,
                        0
                    );
                }
            }
        }
    }
}
