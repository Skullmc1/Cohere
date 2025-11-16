package com.qclid.cohere.Utility;

import com.qclid.cohere.Cohere;
import com.qclid.cohere.Modules.CoherenceManager;
import com.qclid.cohere.Modules.Team.InviteManager;
import com.qclid.cohere.Modules.Team.TeamCommandMessages;
import com.qclid.cohere.Modules.Team.Teams;
import com.qclid.cohere.Shares.PotionShare;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final Cohere plugin;
    private final CoherenceManager coherenceManager;
    private final PotionShare potionShare;
    private final InviteManager inviteManager;
    private final Teams teams;

    public PlayerListener(
        Cohere plugin,
        CoherenceManager coherenceManager,
        PotionShare potionShare,
        InviteManager inviteManager,
        Teams teams
    ) {
        this.plugin = plugin;
        this.coherenceManager = coherenceManager;
        this.potionShare = potionShare;
        this.inviteManager = inviteManager;
        this.teams = teams;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        coherenceManager.removePlayer(event.getPlayer());
        potionShare.cleanUpPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (inviteManager.hasInvite(player)) {
            String teamName = inviteManager.getInviteTeam(player);
            String leaderUUID = teams.getTeamLeader(teamName);
            if (leaderUUID != null && !leaderUUID.isEmpty()) {
                Player leader = Bukkit.getPlayer(UUID.fromString(leaderUUID));
                String leaderName = leader != null
                    ? leader.getName()
                    : "Unknown";
                plugin
                    .adventure()
                    .sender(player)
                    .sendMessage(
                        TeamCommandMessages.getInviteReceivedMessage(
                            teamName,
                            leaderName
                        )
                    );
            }
        }
    }
}
