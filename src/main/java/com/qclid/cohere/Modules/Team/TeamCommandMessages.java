package com.qclid.cohere.Modules.Team;

import com.qclid.cohere.Utility.MinimessageFormatter;
import com.qclid.cohere.Utility.SmallFont;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

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
            SmallFont.toSmallFont("/team create <name> - Creates a new team.") +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont(
                "/team disband - Disbands your current team."
            ) +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont(
                "/team rename <newname> - Renames your team."
            ) +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont(
                "/team invite <player> - Invites a player to your team."
            ) +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont(
                "/team kick <player> - Kicks a player from your team."
            ) +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont(
                "/team particle <id> - Sets your team's particle effect."
            ) +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont("/team leave - Leaves your current team.") +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont(
                "/team transferownership <player> - Transfers ownership of your team."
            ) +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont("/team accept - Accepts a team invite.") +
            "</gray>\n" +
            "<gray>" +
            SmallFont.toSmallFont("/team deny - Denies a team invite.") +
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

    public static Component getPlayerLeftTeamMessage(
        String playerName,
        String teamName
    ) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>" +
                SmallFont.toSmallFont("Player '") +
                SmallFont.toSmallFont(playerName) +
                SmallFont.toSmallFont("' has left team '") +
                SmallFont.toSmallFont(teamName) +
                "'.</gray>"
        );
    }

    public static Component getLeaderCannotLeaveError() {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gradient:#EF6079:#D74C6E>" +
                SmallFont.toSmallFont(
                    "Team leaders must transfer ownership or disband the team before leaving."
                ) +
                "</gradient>"
        );
    }

    public static Component getOwnershipTransferredMessage(
        String newLeaderName
    ) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>" +
                SmallFont.toSmallFont(
                    "You have transferred team ownership to '"
                ) +
                SmallFont.toSmallFont(newLeaderName) +
                "'.</gray>"
        );
    }

    public static Component getInviteSentMessage(String playerName) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>" +
                SmallFont.toSmallFont("Invite sent to '") +
                SmallFont.toSmallFont(playerName) +
                "'.</gray>"
        );
    }

    public static Component getInviteReceivedMessage(
        String teamName,
        String leaderName
    ) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>" +
                SmallFont.toSmallFont("You have been invited to join team '") +
                SmallFont.toSmallFont(teamName) +
                SmallFont.toSmallFont("' by '") +
                SmallFont.toSmallFont(leaderName) +
                "'.</gray>\n" +
                "<gray>" +
                SmallFont.toSmallFont("Type '/team accept' or '/team deny'.") +
                "</gray>"
        );
    }

    public static Component getInviteDeniedMessage(String playerName) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>'" +
                SmallFont.toSmallFont(playerName) +
                SmallFont.toSmallFont("' has denied your team invite.") +
                "</gray>"
        );
    }

    public static Component getNoPendingInviteError() {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gradient:#EF6079:#D74C6E>" +
                SmallFont.toSmallFont("You have no pending invites.") +
                "</gradient>"
        );
    }

    public static Component getInviteAcceptedMessage(String teamName) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>" +
                SmallFont.toSmallFont("You have joined team '") +
                SmallFont.toSmallFont(teamName) +
                "'.</gray>"
        );
    }

    public static Component getPlayerJoinedTeamMessage(String playerName) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>'" +
                SmallFont.toSmallFont(playerName) +
                SmallFont.toSmallFont("' has joined your team.") +
                "</gray>"
        );
    }

    public static Component getOwnershipReceivedMessage(String oldLeaderName) {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gray>" +
                SmallFont.toSmallFont(
                    "You are now the leader of the team, transferred from '"
                ) +
                SmallFont.toSmallFont(oldLeaderName) +
                "'.</gray>"
        );
    }

    public static Component getInvalidParticleIdError() {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gradient:#EF6079:#D74C6E>" +
                SmallFont.toSmallFont("Enter a valid particle id, see ") +
                "<hover:show_text:'<aqua>Click to open link'><click:open_url:'https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html'>" +
                SmallFont.toSmallFont("here") +
                "</click></hover>" +
                SmallFont.toSmallFont(".") +
                "</gradient>"
        );
    }

    public static Component getParticleNeedsDataError() {
        return MinimessageFormatter.format(
            TEAM_PREFIX +
                "<gradient:#EF6079:#D74C6E>" +
                SmallFont.toSmallFont(
                    "You can't use that because it needs extra data, pick something else from "
                ) +
                "<hover:show_text:'<aqua>Click to open link'><click:open_url:'https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html'>" +
                SmallFont.toSmallFont("here") +
                "</click></hover>" +
                SmallFont.toSmallFont(".") +
                "</gradient>"
        );
    }
}
