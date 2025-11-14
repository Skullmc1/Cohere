package com.qclid.cohere.Modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CohereTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            if ("help".startsWith(args[0].toLowerCase())) {
                completions.add("help");
            }
            if ("reload".startsWith(args[0].toLowerCase()) && sender.hasPermission("cohere.reload")) {
                completions.add("reload");
            }
            return completions;
        }
        return null;
    }
}
