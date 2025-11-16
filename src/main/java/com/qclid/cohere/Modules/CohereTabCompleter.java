package com.qclid.cohere.Modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CohereTabCompleter implements TabCompleter {

    private static final List<String> TEAM_SUBCOMMANDS = Arrays.asList(
        "create",
        "disband",
        "rename",
        "invite",
        "kick",
        "particle",
        "leave",
        "transferownership",
        "accept",
        "deny"
    );

    @Override
    public @Nullable List<String> onTabComplete(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String alias,
        @NotNull String[] args
    ) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            if ("help".startsWith(args[0].toLowerCase())) {
                completions.add("help");
            }
            if (
                "reload".startsWith(args[0].toLowerCase()) &&
                sender.hasPermission("cohere.reload")
            ) {
                completions.add("reload");
            }
            if (
                "team".startsWith(args[0].toLowerCase()) &&
                sender.hasPermission("cohere.team")
            ) {
                completions.add("team");
            }
            return completions;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("team")) {
            return TEAM_SUBCOMMANDS.stream()
                .filter(s -> s.startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        } else if (
            args.length == 3 &&
            args[0].equalsIgnoreCase("team") &&
            (args[1].equalsIgnoreCase("invite") ||
                args[1].equalsIgnoreCase("kick"))
        ) {
            return Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .filter(name ->
                    name.toLowerCase().startsWith(args[2].toLowerCase())
                )
                .collect(Collectors.toList());
        }
        return null;
    }
}
