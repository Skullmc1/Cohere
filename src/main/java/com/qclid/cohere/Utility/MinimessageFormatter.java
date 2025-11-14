package com.qclid.cohere.Utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MinimessageFormatter {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static Component format(String message) {
        return miniMessage.deserialize(message);
    }
}
