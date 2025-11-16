package com.qclid.cohere.Modules.Team;

import com.qclid.cohere.Cohere;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class InviteManager {

    private final Cohere plugin;
    private final File invitesFile;
    private final FileConfiguration invitesConfig;

    // <InvitedPlayerUUID, TeamName>
    private final Map<UUID, String> pendingInvites = new ConcurrentHashMap<>();

    public InviteManager(Cohere plugin) {
        this.plugin = plugin;
        this.invitesFile = new File(plugin.getDataFolder(), "invites.yml");
        this.invitesConfig = YamlConfiguration.loadConfiguration(invitesFile);
        loadInvites();
    }

    public void addInvite(Player invited, String teamName) {
        pendingInvites.put(invited.getUniqueId(), teamName);
        saveInvites();
    }

    public void removeInvite(Player invited) {
        pendingInvites.remove(invited.getUniqueId());
        saveInvites();
    }

    public boolean hasInvite(Player invited) {
        return pendingInvites.containsKey(invited.getUniqueId());
    }

    public String getInviteTeam(Player invited) {
        return pendingInvites.get(invited.getUniqueId());
    }

    private void saveInvites() {
        synchronized (this) {
            for (Map.Entry<UUID, String> entry : pendingInvites.entrySet()) {
                invitesConfig.set(entry.getKey().toString(), entry.getValue());
            }
            try {
                invitesConfig.save(invitesFile);
            } catch (IOException e) {
                plugin
                    .getLogger()
                    .log(
                        Level.SEVERE,
                        "Could not save invites to " + invitesFile,
                        e
                    );
            }
        }
    }

    private void loadInvites() {
        synchronized (this) {
            for (String key : invitesConfig.getKeys(false)) {
                pendingInvites.put(
                    UUID.fromString(key),
                    invitesConfig.getString(key)
                );
            }
        }
    }
}
