package com.qclid.cohere.Modules.Team;

import com.qclid.cohere.Cohere;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Teams {

    private final Cohere plugin;
    private FileConfiguration teamsConfig;
    private File teamsFile;

    public Teams(Cohere plugin) {
        this.plugin = plugin;
        loadTeamsConfig();
    }

    public void loadTeamsConfig() {
        teamsFile = new File(plugin.getDataFolder(), "Teams.yml");
        if (!teamsFile.exists()) {
            plugin.saveResource("Teams.yml", false);
        }
        teamsConfig = YamlConfiguration.loadConfiguration(teamsFile);
    }

    public void saveTeamsConfig() {
        try {
            teamsConfig.save(teamsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save Teams.yml!");
            e.printStackTrace();
        }
    }

    public FileConfiguration getTeamsConfig() {
        return teamsConfig;
    }

    public boolean isTeamFeatureEnabled() {
        return teamsConfig.getBoolean("enabled", true);
    }

    public String getPlayerTeam(Player player) {
        UUID playerUUID = player.getUniqueId();
        Set<String> teams = teamsConfig
            .getConfigurationSection("teams")
            .getKeys(false);
        for (String team : teams) {
            List<String> playerUUIDs = teamsConfig.getStringList(
                "teams." + team + ".players"
            );
            if (playerUUIDs.contains(playerUUID.toString())) {
                return team;
            }
        }
        return "default";
    }

    public boolean areInSameTeam(Player player1, Player player2) {
        if (!isTeamFeatureEnabled()) {
            return true; // If feature is disabled, everyone is in the same "team"
        }
        String team1 = getPlayerTeam(player1);
        String team2 = getPlayerTeam(player2);
        return team1.equals(team2);
    }

    public boolean canSharePotions(Player player) {
        String teamName = getPlayerTeam(player);
        return teamsConfig.getBoolean(
            "teams." + teamName + ".allow-potion-sharing",
            false
        );
    }

    public boolean canSharePain(Player player) {
        String teamName = getPlayerTeam(player);
        return teamsConfig.getBoolean(
            "teams." + teamName + ".allow-pain-sharing",
            false
        );
    }

    public boolean canReceiveDeathDebuffs(Player player) {
        String teamName = getPlayerTeam(player);
        return teamsConfig.getBoolean(
            "teams." + teamName + ".allow-death-debuffs",
            false
        );
    }

    public String getTeamParticle(Player player) {
        String teamName = getPlayerTeam(player);
        return teamsConfig.getString(
            "teams." + teamName + ".particle-effect",
            "CRIT"
        );
    }
}
