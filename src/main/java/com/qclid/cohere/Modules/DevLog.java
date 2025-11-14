package com.qclid.cohere.Modules;

import com.qclid.cohere.Cohere;
import java.util.logging.Logger;

public class DevLog {

    private final Cohere plugin;
    private final Logger logger;
    private final String prefix = "[DevLog] ";

    public DevLog(Cohere plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public void log(String message) {
        if (plugin.getConfig().getBoolean("general-settings.dev-logs", false)) {
            logger.info(prefix + message);
        }
    }
}
