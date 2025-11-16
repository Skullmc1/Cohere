package com.qclid.cohere.Utility;

import com.qclid.cohere.Cohere;
import com.qclid.cohere.Modules.Team.InviteManager;
import com.qclid.cohere.Modules.Team.TeamCommandMessages;
import com.qclid.cohere.Modules.Team.Teams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class InviteReminderTask extends BukkitRunnable {

    private final Cohere plugin;
    private final InviteManager inviteManager;
    private final Teams teams;

    public InviteReminderTask(Cohere plugin, InviteManager inviteManager, Teams teams) {
        this.plugin = plugin;
        this.inviteManager = inviteManager;
        this.teams = teams;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (inviteManager.hasInvite(player)) {
                String teamName = inviteManager.getInviteTeam(player);
                String leaderUUID = teams.getTeamLeader(teamName);

                if (teamName == null || leaderUUID == null || leaderUUID.isEmpty()) {
                    // Clean up invalid invite
                    inviteManager.removeInvite(player);
                    continue;
                }

                Player leader = Bukkit.getPlayer(UUID.fromString(leaderUUID));
                String leaderName = "Unknown";
                if(leader != null) {
                    leaderName = leader.getName();
                } else {
                    // If leader is offline, maybe get their name from user cache? For now, "Unknown" is fine.
                }

                plugin.adventure().sender(player).sendMessage(TeamCommandMessages.getInviteReceivedMessage(teamName, leaderName));
                plugin.getDevLog().log("Sent team invite reminder to " + player.getName());
            }
        }
    }
}
