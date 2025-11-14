package com.qclid.cohere.Utility;

import com.qclid.cohere.Modules.CoherenceManager;
import com.qclid.cohere.Modules.PotionShare;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final CoherenceManager coherenceManager;
    private final PotionShare potionShare;

    public PlayerListener(
        CoherenceManager coherenceManager,
        PotionShare potionShare
    ) {
        this.coherenceManager = coherenceManager;
        this.potionShare = potionShare;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        coherenceManager.removePlayer(event.getPlayer());
        potionShare.cleanUpPlayer(event.getPlayer());
    }
}
