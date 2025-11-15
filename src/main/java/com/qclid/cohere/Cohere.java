package com.qclid.cohere;

import com.qclid.cohere.Modules.CohereCommand;
import com.qclid.cohere.Modules.CohereTabCompleter;
import com.qclid.cohere.Modules.CoherenceManager;
import com.qclid.cohere.Modules.DevLog;
import com.qclid.cohere.Modules.GeneralMessages;
import com.qclid.cohere.Modules.Team.TeamCommands;
import com.qclid.cohere.Modules.Team.Teams;
import com.qclid.cohere.Modules.WorldManager;
import com.qclid.cohere.Shares.DeathDebuffs;
import com.qclid.cohere.Shares.PainShare;
import com.qclid.cohere.Shares.PotionShare;
import com.qclid.cohere.Shares.XPShare;
import com.qclid.cohere.Utility.CoherenceTask;
import com.qclid.cohere.Utility.PlayerListener;
import com.qclid.cohere.Utility.VisualEffect;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Cohere extends JavaPlugin {

    private GeneralMessages generalMessages;
    private BukkitAudiences adventure;
    private CoherenceManager coherenceManager;
    private DevLog devLog;
    private WorldManager worldManager;
    private PotionShare potionShare;
    private VisualEffect visualEffect;
    private Teams teams;
    private TeamCommands teamCommands;

    public @NotNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException(
                "Tried to access Adventure when the plugin was disabled!"
            );
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        getLogger().info("Initializing modules...");

        this.worldManager = new WorldManager(this);
        getLogger().info("WorldManager initialized.");

        this.devLog = new DevLog(this);
        getLogger().info("DevLog initialized.");

        this.adventure = BukkitAudiences.create(this);
        getLogger().info("Adventure (BukkitAudiences) initialized.");

        this.generalMessages = new GeneralMessages(this);
        getLogger().info("GeneralMessages initialized.");

        this.coherenceManager = new CoherenceManager(this);
        getLogger().info("CoherenceManager initialized.");

        this.teams = new Teams(this);
        getLogger().info("Teams initialized.");

        this.teamCommands = new TeamCommands(this, teams);
        getLogger().info("TeamCommands initialized.");

        this.visualEffect = new VisualEffect(this.coherenceManager, this.teams);
        getLogger().info("VisualEffect initialized.");

        this.potionShare = new PotionShare(this);
        getLogger().info("PotionShare initialized.");

        getServer().getPluginManager().registerEvents(new XPShare(this), this);
        getLogger().info("XPShare listener registered.");

        getServer()
            .getPluginManager()
            .registerEvents(new PainShare(this), this);
        getLogger().info("PainShare listener registered.");

        getServer()
            .getPluginManager()
            .registerEvents(new DeathDebuffs(this), this);
        getLogger().info("DeathDebuffs listener registered.");

        getServer()
            .getPluginManager()
            .registerEvents(
                new PlayerListener(this.coherenceManager, this.potionShare),
                this
            );
        getLogger().info("PlayerListener registered.");

        new CoherenceTask(
            this.coherenceManager,
            this.potionShare,
            this.visualEffect
        ).runTaskTimer(this, 0L, 20L); // 20 ticks = 1 second
        getLogger().info("CoherenceTask scheduled.");

        getCommand("cohere").setExecutor(new CohereCommand(this, teamCommands));
        getCommand("cohere").setTabCompleter(new CohereTabCompleter());
        getLogger().info("CohereCommand executor and tab completer set.");

        getLogger().info("Cohere plugin enabled successfully.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        getLogger().info("Cohere plugin disabled.");
    }

    public GeneralMessages getGeneralMessages() {
        return generalMessages;
    }

    public CoherenceManager getCoherenceManager() {
        return coherenceManager;
    }

    public DevLog getDevLog() {
        return devLog;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public PotionShare getPotionShare() {
        return potionShare;
    }

    public VisualEffect getVisualEffect() {
        return visualEffect;
    }

    public Teams getTeams() {
        return teams;
    }
}
