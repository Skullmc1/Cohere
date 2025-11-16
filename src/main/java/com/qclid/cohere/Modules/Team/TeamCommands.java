package com.qclid.cohere.Modules.Team;

import com.qclid.cohere.Cohere;
import com.qclid.cohere.Utility.MinimessageFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TeamCommands {

    private final Cohere plugin;
    private final Teams teams;
    private final InviteManager inviteManager;

    public TeamCommands(
        Cohere plugin,
        Teams teams,
        InviteManager inviteManager
    ) {
        this.plugin = plugin;
        this.teams = teams;
        this.inviteManager = inviteManager;
    }

    public void handleCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use team commands.");
            return;
        }

        Player player = (Player) sender;
        String subCommand;

        if (args.length == 1 && args[0].equalsIgnoreCase("team")) {
            plugin
                .adventure()
                .sender(sender)
                .sendMessage(TeamCommandMessages.getTeamHelpMessage());
            return;
        } else if (args.length > 1) {
            subCommand = args[1].toLowerCase();
        } else {
            plugin
                .adventure()
                .sender(sender)
                .sendMessage(TeamCommandMessages.getTeamHelpMessage());
            return;
        }

        String teamName = teams.getPlayerTeam(player);
        boolean isLeader = teams.isTeamLeader(player, teamName);

        switch (subCommand) {
            case "create":
                createTeam(player, args);
                break;
            case "disband":
                if (teamName.equals("default")) {
                    plugin
                        .adventure()
                        .sender(player)
                        .sendMessage(
                            TeamCommandMessages.getError(
                                "You are not in a team."
                            )
                        );
                    return;
                }
                if (!isLeader) {
                    plugin
                        .adventure()
                        .sender(player)
                        .sendMessage(
                            TeamCommandMessages.getError(
                                "Only the team leader can disband the team."
                            )
                        );
                    return;
                }
                disbandTeam(player);
                break;
            case "rename":
                if (teamName.equals("default")) {
                    plugin
                        .adventure()
                        .sender(player)
                        .sendMessage(
                            TeamCommandMessages.getError(
                                "You are not in a team."
                            )
                        );
                    return;
                }
                if (!isLeader) {
                    plugin
                        .adventure()
                        .sender(player)
                        .sendMessage(
                            TeamCommandMessages.getError(
                                "Only the team leader can rename the team."
                            )
                        );
                    return;
                }
                renameTeam(player, args);
                break;
            case "invite": // Renamed from "add"
                if (teamName.equals("default")) {
                    plugin
                        .adventure()
                        .sender(player)
                        .sendMessage(
                            TeamCommandMessages.getError(
                                "You must be in a team to invite players."
                            )
                        );
                    return;
                }
                if (!isLeader) {
                    plugin
                        .adventure()
                        .sender(player)
                        .sendMessage(
                            TeamCommandMessages.getError(
                                "Only the team leader can invite players."
                            )
                        );
                    return;
                }
                invitePlayer(player, args);
                break;
            case "kick": // Renamed from "remove"
                if (teamName.equals("default")) {
                    plugin
                        .adventure()
                        .sender(player)
                        .sendMessage(
                            TeamCommandMessages.getError(
                                "You are not in a team."
                            )
                        );
                    return;
                }
                if (!isLeader) {
                    plugin
                        .adventure()
                        .sender(player)
                        .sendMessage(
                            TeamCommandMessages.getError(
                                "Only the team leader can kick players."
                            )
                        );
                    return;
                }
                kickPlayer(player, args);
                break;
            case "particle":
                if (teamName.equals("default")) {
                    plugin
                        .adventure()
                        .sender(player)
                        .sendMessage(
                            TeamCommandMessages.getError(
                                "You must be in a team to set a particle."
                            )
                        );
                    return;
                }
                if (!isLeader) {
                    plugin
                        .adventure()
                        .sender(player)
                        .sendMessage(
                            TeamCommandMessages.getError(
                                "Only the team leader can set the particle effect."
                            )
                        );
                    return;
                }
                setParticle(player, args);
                break;
            case "leave":
                if (teamName.equals("default")) {
                    plugin
                        .adventure()
                        .sender(player)
                        .sendMessage(
                            TeamCommandMessages.getError(
                                "You are not in a team."
                            )
                        );
                    return;
                }
                leaveTeam(player);
                break;
            case "transferownership":
                if (teamName.equals("default")) {
                    plugin
                        .adventure()
                        .sender(player)
                        .sendMessage(
                            TeamCommandMessages.getError(
                                "You are not in a team."
                            )
                        );
                    return;
                }
                if (!isLeader) {
                    plugin
                        .adventure()
                        .sender(player)
                        .sendMessage(
                            TeamCommandMessages.getError(
                                "Only the team leader can transfer ownership."
                            )
                        );
                    return;
                }
                transferOwnership(player, args);
                break;
            case "accept":
                acceptInvite(player);
                break;
            case "deny":
                denyInvite(player);
                break;
            default:
                plugin
                    .adventure()
                    .sender(sender)
                    .sendMessage(TeamCommandMessages.getTeamHelpMessage());
                break;
        }
    }

    private void createTeam(Player player, String[] args) {
        if (args.length < 3) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "Usage: /ch team create <name>"
                    )
                );
            return;
        }
        String teamName = args[2];
        FileConfiguration config = teams.getTeamsConfig();

        if (config.getConfigurationSection("teams." + teamName) != null) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "A team with that name already exists."
                    )
                );
            return;
        }

        String currentTeam = teams.getPlayerTeam(player);
        if (!currentTeam.equals("default")) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "You are already in a team. Leave your current team first."
                    )
                );
            return;
        }

        List<String> defaultPlayers = config.getStringList(
            "teams.default.players"
        );
        defaultPlayers.remove(player.getUniqueId().toString());
        config.set("teams.default.players", defaultPlayers);

        String playerUUID = player.getUniqueId().toString();
        config.set("teams." + teamName + ".leader", playerUUID);
        config.set("teams." + teamName + ".allow-potion-sharing", true);
        config.set("teams." + teamName + ".allow-pain-sharing", true);
        config.set("teams." + teamName + ".allow-death-debuffs", true);
        config.set("teams." + teamName + ".particle-effect", "HEART");
        List<String> players = new ArrayList<>();
        players.add(playerUUID);
        config.set("teams." + teamName + ".players", players);

        teams.saveTeamsConfig();
        plugin
            .adventure()
            .sender(player)
            .sendMessage(TeamCommandMessages.getTeamCreatedMessage(teamName));
    }

    private void disbandTeam(Player player) {
        String teamName = teams.getPlayerTeam(player);
        if (teamName.equals("default")) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError("You are not in a team.")
                );
            return;
        }

        if (!teams.isTeamLeader(player, teamName)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "Only the team leader can disband the team."
                    )
                );
            return;
        }

        FileConfiguration config = teams.getTeamsConfig();
        List<String> teamPlayers = config.getStringList(
            "teams." + teamName + ".players"
        );

        List<String> defaultPlayers = config.getStringList(
            "teams.default.players"
        );
        defaultPlayers.addAll(teamPlayers);
        config.set("teams.default.players", defaultPlayers);

        config.set("teams." + teamName, null);
        teams.saveTeamsConfig();

        // Notify all disbanded players
        for (String uuid : teamPlayers) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if (p != null) {
                plugin
                    .adventure()
                    .sender(p)
                    .sendMessage(
                        TeamCommandMessages.getTeamDisbandedMessage(teamName)
                    );
            }
        }
    }

    private void renameTeam(Player player, String[] args) {
        if (args.length < 3) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "Usage: /ch team rename <newname>"
                    )
                );
            return;
        }
        String newName = args[2];
        String oldName = teams.getPlayerTeam(player);

        if (oldName.equals("default")) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "You cannot rename the default team."
                    )
                );
            return;
        }

        if (!teams.isTeamLeader(player, oldName)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "Only the team leader can rename the team."
                    )
                );
            return;
        }

        FileConfiguration config = teams.getTeamsConfig();
        if (config.getConfigurationSection("teams." + newName) != null) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "A team with that name already exists."
                    )
                );
            return;
        }

        config.set(
            "teams." + newName,
            config.getConfigurationSection("teams." + oldName)
        );
        config.set("teams." + oldName, null);
        teams.saveTeamsConfig();

        plugin
            .adventure()
            .sender(player)
            .sendMessage(
                TeamCommandMessages.getTeamRenamedMessage(oldName, newName)
            );
    }

    private void invitePlayer(Player player, String[] args) {
        if (args.length < 3) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "Usage: /ch team invite <player>"
                    )
                );
            return;
        }
        String teamName = teams.getPlayerTeam(player);
        if (teamName.equals("default")) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "You must be in a team to invite players."
                    )
                );
            return;
        }

        if (!teams.isTeamLeader(player, teamName)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "Only the team leader can invite players."
                    )
                );
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(TeamCommandMessages.getError("Player not found."));
            return;
        }

        if (player.equals(target)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError("You cannot invite yourself.")
                );
            return;
        }

        if (!teams.getPlayerTeam(target).equals("default")) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "That player is already in a team."
                    )
                );
            return;
        }

        inviteManager.addInvite(target, teamName);
        plugin
            .adventure()
            .sender(player)
            .sendMessage(
                TeamCommandMessages.getInviteSentMessage(target.getName())
            );
        plugin
            .adventure()
            .sender(target)
            .sendMessage(
                TeamCommandMessages.getInviteReceivedMessage(
                    teamName,
                    player.getName()
                )
            );
    }

    private void kickPlayer(Player player, String[] args) {
        if (args.length < 3) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "Usage: /ch team kick <player>"
                    )
                );
            return;
        }
        String teamName = teams.getPlayerTeam(player);
        if (teamName.equals("default")) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError("You are not in a team.")
                );
            return;
        }

        if (!teams.isTeamLeader(player, teamName)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "Only the team leader can kick players."
                    )
                );
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(TeamCommandMessages.getError("Player not found."));
            return;
        }

        if (player.equals(target)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError("You cannot kick yourself.")
                );
            return;
        }

        String targetTeam = teams.getPlayerTeam(target);
        if (!targetTeam.equals(teamName)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "That player is not in your team."
                    )
                );
            return;
        }

        FileConfiguration config = teams.getTeamsConfig();

        List<String> teamPlayers = config.getStringList(
            "teams." + teamName + ".players"
        );
        teamPlayers.remove(target.getUniqueId().toString());
        config.set("teams." + teamName + ".players", teamPlayers);

        List<String> defaultPlayers = config.getStringList(
            "teams.default.players"
        );
        defaultPlayers.add(target.getUniqueId().toString());
        config.set("teams.default.players", defaultPlayers);

        teams.saveTeamsConfig();
        plugin
            .adventure()
            .sender(player)
            .sendMessage(
                TeamCommandMessages.getPlayerRemovedMessage(
                    target.getName(),
                    teamName
                )
            );
        plugin
            .adventure()
            .sender(target)
            .sendMessage(
                Component.text(
                    "You have been kicked from team '" + teamName + "'."
                )
            );
    }

    private void leaveTeam(Player player) {
        String teamName = teams.getPlayerTeam(player);
        if (teamName.equals("default")) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError("You are not in a team.")
                );
            return;
        }

        if (teams.isTeamLeader(player, teamName)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(TeamCommandMessages.getLeaderCannotLeaveError());
            return;
        }

        FileConfiguration config = teams.getTeamsConfig();
        List<String> teamPlayers = config.getStringList(
            "teams." + teamName + ".players"
        );
        teamPlayers.remove(player.getUniqueId().toString());
        config.set("teams." + teamName + ".players", teamPlayers);

        List<String> defaultPlayers = config.getStringList(
            "teams.default.players"
        );
        defaultPlayers.add(player.getUniqueId().toString());
        config.set("teams.default.players", defaultPlayers);

        teams.saveTeamsConfig();
        plugin
            .adventure()
            .sender(player)
            .sendMessage(
                TeamCommandMessages.getPlayerLeftTeamMessage(
                    player.getName(),
                    teamName
                )
            );

        // Notify leader
        String leaderUUID = teams.getTeamLeader(teamName);
        if (!leaderUUID.isEmpty()) {
            Player leader = Bukkit.getPlayer(UUID.fromString(leaderUUID));
            if (leader != null) {
                plugin
                    .adventure()
                    .sender(leader)
                    .sendMessage(
                        TeamCommandMessages.getPlayerLeftTeamMessage(
                            player.getName(),
                            teamName
                        )
                    );
            }
        }
    }

    private void transferOwnership(Player player, String[] args) {
        if (args.length < 3) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "Usage: /ch team transferownership <player>"
                    )
                );
            return;
        }

        String teamName = teams.getPlayerTeam(player);
        if (teamName.equals("default")) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError("You are not in a team.")
                );
            return;
        }

        if (!teams.isTeamLeader(player, teamName)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "Only the team leader can transfer ownership."
                    )
                );
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(TeamCommandMessages.getError("Player not found."));
            return;
        }

        if (player.equals(target)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError("You are already the leader.")
                );
            return;
        }

        String targetTeam = teams.getPlayerTeam(target);
        if (!targetTeam.equals(teamName)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "That player is not in your team."
                    )
                );
            return;
        }

        teams
            .getTeamsConfig()
            .set(
                "teams." + teamName + ".leader",
                target.getUniqueId().toString()
            );
        teams.saveTeamsConfig();

        plugin
            .adventure()
            .sender(player)
            .sendMessage(
                TeamCommandMessages.getOwnershipTransferredMessage(
                    target.getName()
                )
            );
        plugin
            .adventure()
            .sender(target)
            .sendMessage(
                TeamCommandMessages.getOwnershipReceivedMessage(
                    player.getName()
                )
            );
    }

    private void acceptInvite(Player player) {
        if (!inviteManager.hasInvite(player)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(TeamCommandMessages.getNoPendingInviteError());
            return;
        }

        String teamName = inviteManager.getInviteTeam(player);
        FileConfiguration config = teams.getTeamsConfig();

        // Ensure team still exists
        if (config.getConfigurationSection("teams." + teamName) == null) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "The team you were invited to no longer exists."
                    )
                );
            inviteManager.removeInvite(player);
            return;
        }

        // Remove from default team
        List<String> defaultPlayers = config.getStringList(
            "teams.default.players"
        );
        defaultPlayers.remove(player.getUniqueId().toString());
        config.set("teams.default.players", defaultPlayers);

        // Add to new team
        List<String> teamPlayers = config.getStringList(
            "teams." + teamName + ".players"
        );
        teamPlayers.add(player.getUniqueId().toString());
        config.set("teams." + teamName + ".players", teamPlayers);

        teams.saveTeamsConfig();
        inviteManager.removeInvite(player);

        plugin
            .adventure()
            .sender(player)
            .sendMessage(
                TeamCommandMessages.getInviteAcceptedMessage(teamName)
            );

        // Notify leader
        String leaderUUID = teams.getTeamLeader(teamName);
        if (!leaderUUID.isEmpty()) {
            Player leader = Bukkit.getPlayer(UUID.fromString(leaderUUID));
            if (leader != null) {
                plugin
                    .adventure()
                    .sender(leader)
                    .sendMessage(
                        TeamCommandMessages.getPlayerJoinedTeamMessage(
                            player.getName()
                        )
                    );
            }
        }
    }

    private void denyInvite(Player player) {
        if (!inviteManager.hasInvite(player)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(TeamCommandMessages.getNoPendingInviteError());
            return;
        }

        String teamName = inviteManager.getInviteTeam(player);

        // Notify leader
        String leaderUUID = teams.getTeamLeader(teamName);
        if (!leaderUUID.isEmpty()) {
            Player leader = Bukkit.getPlayer(UUID.fromString(leaderUUID));
            if (leader != null) {
                plugin
                    .adventure()
                    .sender(leader)
                    .sendMessage(
                        TeamCommandMessages.getInviteDeniedMessage(
                            player.getName()
                        )
                    );
            }
        }

        inviteManager.removeInvite(player);
        plugin
            .adventure()
            .sender(player)
            .sendMessage(
                MinimessageFormatter.format(
                    "<gray>You have denied the invite.</gray>"
                )
            );
    }

    private void setParticle(Player player, String[] args) {
        if (args.length < 3) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "Usage: /ch team particle <id>"
                    )
                );
            return;
        }
        String teamName = teams.getPlayerTeam(player);
        if (teamName.equals("default")) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "You must be in a team to set a particle."
                    )
                );
            return;
        }

        if (!teams.isTeamLeader(player, teamName)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "Only the team leader can set the particle effect."
                    )
                );
            return;
        }

        String particleId = args[2].toUpperCase();
        Particle particle;
        try {
            particle = org.bukkit.Particle.valueOf(particleId);
        } catch (IllegalArgumentException e) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(TeamCommandMessages.getInvalidParticleIdError());
            return;
        }

        final Set<Particle> disallowedParticles = Set.of(
            Particle.BLOCK,
            Particle.BLOCK_MARKER,
            Particle.DRAGON_BREATH,
            Particle.DUST_PILLAR,
            Particle.EFFECT,
            Particle.ENTITY_EFFECT,
            Particle.FALLING_DUST,
            Particle.FLASH,
            Particle.ITEM,
            Particle.DUST,
            Particle.DUST_COLOR_TRANSITION,
            Particle.VIBRATION,
            Particle.SHRIEK,
            Particle.SCULK_CHARGE,
            Particle.INSTANT_EFFECT
        );

        if (disallowedParticles.contains(particle)) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(TeamCommandMessages.getParticleNeedsDataError());
            return;
        }

        teams
            .getTeamsConfig()
            .set("teams." + teamName + ".particle-effect", particleId);
        teams.saveTeamsConfig();
        plugin
            .adventure()
            .sender(player)
            .sendMessage(
                TeamCommandMessages.getParticleSetMessage(particleId, teamName)
            );
    }
}
