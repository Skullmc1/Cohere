package com.qclid.cohere.Modules;

import com.qclid.cohere.Cohere;
import com.qclid.cohere.Modules.Team.TeamCommands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CohereCommand implements CommandExecutor {

    private final Cohere plugin;
    private final TeamCommands teamCommands;

    public CohereCommand(Cohere plugin, TeamCommands teamCommands) {
        this.plugin = plugin;
        this.teamCommands = teamCommands;
    }

    @Override
    public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        if (label.equalsIgnoreCase("team")) {
            String[] newArgs = new String[args.length + 1];
            newArgs[0] = "team";
            System.arraycopy(args, 0, newArgs, 1, args.length);
            args = newArgs;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("cohere.reload")) {
                    plugin.reloadConfig();
                    plugin.getTeams().loadTeamsConfig();
                    plugin.getWorldManager().loadDisabledWorlds();
                    plugin.getGeneralMessages().sendReloadMessage(sender);
                } else {
                    plugin
                        .adventure()
                        .sender(sender)
                        .sendMessage(
                            Component.text(
                                "You do not have permission to do that.",
                                NamedTextColor.RED
                            )
                        );
                }
                return true;
            } else if (args[0].equalsIgnoreCase("team")) {
                teamCommands.handleCommand(sender, args);
                return true;
            }
        }

        plugin.getGeneralMessages().sendHelpMessage(sender);
        return true;
    }
}
