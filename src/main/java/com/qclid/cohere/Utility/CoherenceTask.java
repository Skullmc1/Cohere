package com.qclid.cohere.Utility;

import com.qclid.cohere.Modules.CoherenceManager;
import com.qclid.cohere.Shares.PotionShare;

import org.bukkit.scheduler.BukkitRunnable;

public class CoherenceTask extends BukkitRunnable {

    private final CoherenceManager coherenceManager;
    private final PotionShare potionShare;
    private int tickCounter = 0;

    public CoherenceTask(
        CoherenceManager coherenceManager,
        PotionShare potionShare
    ) {
        this.coherenceManager = coherenceManager;
        this.potionShare = potionShare;
    }

    @Override
    public void run() {
        // Runs every second
        coherenceManager.updateCoherence();

        // Runs every 4 seconds
        if (tickCounter % 4 == 0) {
            potionShare.update();
        }

        tickCounter++;
    }
}
