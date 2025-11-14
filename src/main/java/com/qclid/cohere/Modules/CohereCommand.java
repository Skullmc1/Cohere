package com.qclid.cohere.Modules;

import com.qclid.cohere.Cohere;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CohereCommand implements CommandExecutor {

    private final Cohere plugin;

    public CohereCommand(Cohere plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("cohere.reload")) {
                plugin.reloadConfig();
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
        }

        plugin.getGeneralMessages().sendHelpMessage(sender);
        return true;
    }
}
