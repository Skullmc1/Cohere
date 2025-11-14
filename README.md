# ✨ Cohere ✨

![Plugin Version](https://img.shields.io/badge/Version-1.0.0-blue.svg)
![Supported Spigot Versions](https://img.shields.io/badge/Spigot-1.19+-orange.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

---

## 📖 Overview

**Cohere** is a lightweight and highly configurable Spigot plugin designed to reward players for sticking together. It creates a 'coherence' effect, allowing nearby players to share perks like XP and potion effects, fostering teamwork and collaboration on your server.

<br>

## 🚀 Features

*   **🌐 Proximity-Based Coherence:** Automatically detects nearby players and enables sharing. The minimum distance required is fully configurable.

*   **✨ Advanced XP Sharing:**
    *   **Add Mode:** Grants bonus XP to nearby players without taking from the original earner.
    *   **Subtract Mode:** Divides the earned XP evenly among all players in coherence.
    *   *Fully configurable, with options to toggle the feature and set the sharing percentage.*

*   **🧪 Dynamic Potion Sharing:**
    *   Shares active potion effects with nearby players.
    *   Configurable level decrease for shared effects (e.g., a Level II effect is shared as Level I).
    *   Shared effects have a temporary, refreshing duration.
    *   Intelligently handles effect stacking (prioritizing stronger effects) and prevents messy feedback loops.

*   **🎨 Unique Themed Messaging:**
    *   A beautiful, customizable prefix with gradient colors.
    *   A unique "small-font" style for chat messages.
    *   Automatic, stylized messages for entering/leaving coherence, sharing XP, and more.

*   **⚙️ Highly Configurable:**
    *   Enable or disable any sharing feature with a simple toggle.
    *   Disable all plugin effects in specific worlds (e.g., your server lobby).
    *   Toggle detailed developer logs for easy debugging.

<br>

## 📦 Installation

1.  Download the latest `Cohere.jar` from the releases page.
2.  Place the `.jar` file into your server's `plugins/` folder.
3.  Restart or reload your server. The default configuration file will be generated automatically.

<br>

## 🛠️ Commands & Permissions

| Command          | Description                             | Permission        |
| :--------------- | :-------------------------------------- | :---------------- |
| `/cohere help`   | Displays the plugin's help message.     | `(none)`          |
| `/cohere reload` | Reloads the `config.yml` file.          | `cohere.reload`   |

<br>

## ⚙️ Configuration (`config.yml`)

The `config.yml` file is automatically generated and allows for deep customization of every feature.

```yaml
# General settings for the plugin's core functionality
general-settings:
  min-distance: 10
  disabled-worlds:
    - "lobby"
  dev-logs: true # Master toggle for all dev logs

# Customize the messages sent by the plugin
message-formats:
  help-message: "<gradient:#5e4fa2:#f79459>Cohere Plugin Help</gradient>\n/cohere help - Shows this message."
  reload-message: "<green>Cohere plugin reloaded successfully.</green>"

# Configure the different sharing modules
coherences:
  # --- XP Sharing ---
  xpshare:
    enabled: true
    # Type of sharing: 'add' or 'subtract'
    # 'add': Clones a percentage of the XP to nearby players without reducing the original amount.
    # 'subtract': Divides the XP evenly among nearby players.
    type: "add"
    # Percentage of XP to be shared with nearby players (only for 'add' type).
    shared-percentage: 50
  
  # --- Potion Effect Sharing ---
  potionshare:
    enabled: true
    # The amount to decrease the potion effect level by when sharing.
    # e.g., A value of 1 shares Strength II as Strength I.
    level-decrease: 1
```
