package com.qclid.cohere.Modules.Team;

import com.qclid.cohere.Cohere;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TeamCommands {

    private final Cohere plugin;
    private final Teams teams;

    public TeamCommands(Cohere plugin, Teams teams) {
        this.plugin = plugin;
        this.teams = teams;
    }

    public void handleCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use team commands.");
            return;
        }

        if (args.length < 2) {
            plugin
                .adventure()
                .sender(sender)
                .sendMessage(TeamCommandMessages.getTeamHelpMessage());
            return;
        }

        Player player = (Player) sender;
        String subCommand = args[1].toLowerCase();

        switch (subCommand) {
            case "create":
                createTeam(player, args);
                break;
            case "disband":
                disbandTeam(player);
                break;
            case "rename":
                renameTeam(player, args);
                break;
            case "add":
                addPlayer(player, args);
                break;
            case "remove":
                removePlayer(player, args);
                break;
            case "particle":
                setParticle(player, args);
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

        // Remove from default team if they are in it
        List<String> defaultPlayers = config.getStringList(
            "teams.default.players"
        );
        defaultPlayers.remove(player.getUniqueId().toString());
        config.set("teams.default.players", defaultPlayers);

        config.set("teams." + teamName + ".allow-potion-sharing", true);
        config.set("teams." + teamName + ".allow-pain-sharing", true);
        config.set("teams." + teamName + ".allow-death-debuffs", true);
        config.set("teams." + teamName + ".particle-effect", "HEART");
        List<String> players = new ArrayList<>();
        players.add(player.getUniqueId().toString());
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

        FileConfiguration config = teams.getTeamsConfig();
        List<String> teamPlayers = config.getStringList(
            "teams." + teamName + ".players"
        );

        // Move all players to the default team
        List<String> defaultPlayers = config.getStringList(
            "teams.default.players"
        );
        defaultPlayers.addAll(teamPlayers);
        config.set("teams.default.players", defaultPlayers);

        config.set("teams." + teamName, null); // Delete the team section
        teams.saveTeamsConfig();

        plugin
            .adventure()
            .sender(player)
            .sendMessage(TeamCommandMessages.getTeamDisbandedMessage(teamName));
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

    private void addPlayer(Player player, String[] args) {
        if (args.length < 3) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError("Usage: /ch team add <player>")
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
                        "You must be in a team to add players."
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

        String targetTeam = teams.getPlayerTeam(target);
        if (!targetTeam.equals("default")) {
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

        FileConfiguration config = teams.getTeamsConfig();

        // Remove from default team
        List<String> defaultPlayers = config.getStringList(
            "teams.default.players"
        );
        defaultPlayers.remove(target.getUniqueId().toString());
        config.set("teams.default.players", defaultPlayers);

        // Add to new team
        List<String> teamPlayers = config.getStringList(
            "teams." + teamName + ".players"
        );
        teamPlayers.add(target.getUniqueId().toString());
        config.set("teams." + teamName + ".players", teamPlayers);

        teams.saveTeamsConfig();
        plugin
            .adventure()
            .sender(player)
            .sendMessage(
                TeamCommandMessages.getPlayerAddedMessage(
                    target.getName(),
                    teamName
                )
            );
        plugin
            .adventure()
            .sender(target)
            .sendMessage(
                Component.text(
                    "You have been added to team '" + teamName + "'."
                )
            );
    }

    private void removePlayer(Player player, String[] args) {
        if (args.length < 3) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError(
                        "Usage: /ch team remove <player>"
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

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(TeamCommandMessages.getError("Player not found."));
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

        // Remove from team
        List<String> teamPlayers = config.getStringList(
            "teams." + teamName + ".players"
        );
        teamPlayers.remove(target.getUniqueId().toString());
        config.set("teams." + teamName + ".players", teamPlayers);

        // Add to default team
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
                    "You have been removed from team '" + teamName + "'."
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

        String particleId = args[2].toUpperCase();
        try {
            org.bukkit.Particle.valueOf(particleId);
        } catch (IllegalArgumentException e) {
            plugin
                .adventure()
                .sender(player)
                .sendMessage(
                    TeamCommandMessages.getError("Invalid particle ID.")
                );
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
