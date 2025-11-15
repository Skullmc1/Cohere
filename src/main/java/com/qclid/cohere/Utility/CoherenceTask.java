package com.qclid.cohere.Utility;

import com.qclid.cohere.Modules.CoherenceManager;
import com.qclid.cohere.Shares.PotionShare;
import com.qclid.cohere.Utility.VisualEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CoherenceTask extends BukkitRunnable {

    private final CoherenceManager coherenceManager;
    private final PotionShare potionShare;
    private final VisualEffect visualEffect;
    private int tickCounter = 0;

    public CoherenceTask(
        CoherenceManager coherenceManager,
        PotionShare potionShare,
        VisualEffect visualEffect
    ) {
        this.coherenceManager = coherenceManager;
        this.potionShare = potionShare;
        this.visualEffect = visualEffect;
    }

    @Override
    public void run() {
        // Runs every second
        coherenceManager.updateCoherence();

        for (Player player : Bukkit.getOnlinePlayers()) {
            visualEffect.displayEffect(player);
        }

        // Runs every 4 seconds
        if (tickCounter % 4 == 0) {
            potionShare.update();
        }

        tickCounter++;
    }
}
