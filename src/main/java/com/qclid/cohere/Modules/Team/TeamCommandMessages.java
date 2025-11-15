package com.qclid.cohere.Modules.Team;

import com.qclid.cohere.Utility.MinimessageFormatter;
import com.qclid.cohere.Utility.SmallFont;
import net.kyori.adventure.text.Component;

public class TeamCommandMessages {

    private static final String TEAM_PREFIX =
        "<gradient:#A279B9:#6D7CE6>" +
        SmallFont.toSmallFont("[Cohere Teams]") +
        "</gradient> ";

    public static Component getTeamCreatedMessage(String teamName) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>" +
                SmallFont.toSmallFont("Team '") +
                SmallFont.toSmallFont(teamName) +
                SmallFont.toSmallFont("' has been created.") +
                "</gray>"
        );
    }

    public static Component getTeamDisbandedMessage(String teamName) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>" +
                SmallFont.toSmallFont("Team '") +
                SmallFont.toSmallFont(teamName) +
                SmallFont.toSmallFont("' has been disbanded.") +
                "</gray>"
        );
    }

    public static Component getTeamRenamedMessage(
        String oldName,
        String newName
    ) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>" +
                SmallFont.toSmallFont("Team '") +
                SmallFont.toSmallFont(oldName) +
                SmallFont.toSmallFont("' has been renamed to '") +
                SmallFont.toSmallFont(newName) +
                "'.</gray>"
        );
    }

    public static Component getPlayerAddedMessage(
        String playerName,
        String teamName
    ) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>" +
                SmallFont.toSmallFont("Player '") +
                SmallFont.toSmallFont(playerName) +
                SmallFont.toSmallFont("' has been added to team '") +
                SmallFont.toSmallFont(teamName) +
                "'.</gray>"
        );
    }

    public static Component getPlayerRemovedMessage(
        String playerName,
        String teamName
    ) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>" +
                SmallFont.toSmallFont("Player '") +
                SmallFont.toSmallFont(playerName) +
                SmallFont.toSmallFont("' has been removed from team '") +
                SmallFont.toSmallFont(teamName) +
                "'.</gray>"
        );
    }

    public static Component getParticleSetMessage(
        String particle,
        String teamName
    ) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>" +
                SmallFont.toSmallFont("Particle for team '") +
                teamName +
                SmallFont.toSmallFont("' has been set to '") +
                particle +
                "'.</gray>"
        );
    }

    public static Component getTeamHelpMessage() {
        String helpMessage =
            TEAM_PREFIX +
            "<gradient:#F8EF00:#FDFF87:#F8A300>" +
            SmallFont.toSmallFont("Team Commands Help") +
            "</gradient>\n" +
            "<gray>" +
            SmallFont.toSmallFont(
                "/ch team create <name> - Creates a new team."
            ) +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont(
                "/ch team disband - Disbands your current team."
            ) +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont(
                "/ch team rename <newname> - Renames your team."
            ) +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont(
                "/ch team add <player> - Adds a player to your team."
            ) +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont(
                "/ch team remove <player> - Removes a player from your team."
            ) +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont(
                "/ch team particle <id> - Sets your team's particle effect."
            ) +
            "</gray>";
        return MinimessageFormatter.format(helpMessage);
    }

    public static Component getError(String message) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gradient:#EF6079:#D74C6E>" +
                SmallFont.toSmallFont(message) +
                "</gradient>"
        );
    }
}
