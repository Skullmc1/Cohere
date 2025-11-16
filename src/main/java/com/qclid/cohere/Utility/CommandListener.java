package com.qclid.cohere.Utility;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase();
        String[] parts = command.split(" ");

        if (parts.length > 0 && parts[0].equals("/team")) {
            // Cancel the original /team command
            event.setCancelled(true);

            // Construct the new command (/ch team ...)
            StringBuilder newCommandBuilder = new StringBuilder("ch team");
            for (int i = 1; i < parts.length; i++) {
                newCommandBuilder.append(" ").append(parts[i]);
            }

            // Execute the new command as the player
            player.performCommand(newCommandBuilder.toString());
        }
    }
}
