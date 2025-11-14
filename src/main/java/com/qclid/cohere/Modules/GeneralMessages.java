package com.qclid.cohere.Modules;

import com.qclid.cohere.Cohere;
import com.qclid.cohere.Utility.MinimessageFormatter;
import com.qclid.cohere.Utility.SmallFont;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GeneralMessages {

    private final Cohere plugin;

    public GeneralMessages(Cohere plugin) {
        this.plugin = plugin;
    }

    private String getFormattedPrefix() {
        String prefixText = "[Cohere] ";
        String smallFontPrefix = SmallFont.toSmallFont(prefixText);
        return "<gradient:#f38e37:#fcde31>" + smallFontPrefix + "</gradient>";
    }

    public void sendHelpMessage(CommandSender sender) {
        String message = plugin
            .getConfig()
            .getString(
                "message-formats.help-message",
                "<red>Help message not found in config.yml</red>"
            );
        String finalMessage = getFormattedPrefix() + message;
        plugin
            .adventure()
            .sender(sender)
            .sendMessage(MinimessageFormatter.format(finalMessage));
    }

    public void sendReloadMessage(CommandSender sender) {
        String message = plugin
            .getConfig()
            .getString(
                "message-formats.reload-message",
                "<green>Plugin reloaded.</green>"
            );
        String finalMessage = getFormattedPrefix() + message;
        plugin
            .adventure()
            .sender(sender)
            .sendMessage(MinimessageFormatter.format(finalMessage));
    }

    public void sendCoherenceMessage(Player toPlayer, String fromPlayerName) {
        String message = "You are in coherence with " + fromPlayerName;
        String smallFontMessage = SmallFont.toSmallFont(message);
        net.kyori.adventure.text.Component formattedMessage =
            MinimessageFormatter.format("<gray>" + smallFontMessage);
        plugin.adventure().player(toPlayer).sendMessage(formattedMessage);
    }

    public void sendLeaveCoherenceMessage(
        Player toPlayer,
        String fromPlayerName
    ) {
        String message =
            "You are no longer in coherence with " + fromPlayerName;
        String smallFontMessage = SmallFont.toSmallFont(message);
        net.kyori.adventure.text.Component formattedMessage =
            MinimessageFormatter.format("<gray>" + smallFontMessage);
        plugin.adventure().player(toPlayer).sendMessage(formattedMessage);
    }

    public void sendXPShareMessage(Player toPlayer, String message) {
        String smallFontMessage = SmallFont.toSmallFont(message);
        String finalMessage =
            getFormattedPrefix() + "<green>" + smallFontMessage;
        net.kyori.adventure.text.Component formattedMessage =
            MinimessageFormatter.format(finalMessage);
        plugin.adventure().player(toPlayer).sendMessage(formattedMessage);
    }
}
