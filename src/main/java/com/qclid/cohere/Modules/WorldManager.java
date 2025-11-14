package com.qclid.cohere.Modules;

import com.qclid.cohere.Cohere;
import org.bukkit.World;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class WorldManager {

    private final Cohere plugin;
    private final Set<String> disabledWorlds = new HashSet<>();

    public WorldManager(Cohere plugin) {
        this.plugin = plugin;
        loadDisabledWorlds();
    }

    public void loadDisabledWorlds() {
        disabledWorlds.clear();
        List<String> worlds = plugin.getConfig().getStringList("general-settings.disabled-worlds");
        for (String world : worlds) {
            disabledWorlds.add(world.toLowerCase());
        }
        plugin.getLogger().info("Loaded " + disabledWorlds.size() + " disabled worlds.");
    }

    public boolean isWorldDisabled(World world) {
        return disabledWorlds.contains(world.getName().toLowerCase());
    }
}
