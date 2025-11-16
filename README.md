# Cohere Plugin

Cohere is a Spigot plugin that links players together, allowing them to share XP, effects, and custom abilities. When players team up, their combined strength grows, and they can work together to overcome challenges.

## Features

*   **Teams:** Players can create and join teams, allowing them to collaborate and share abilities.
*   **Coherence:** When players are close to each other, they enter a state of "coherence," which enhances their shared abilities.
*   **Pain Share:** When a player in a team takes damage, a portion of that damage is shared with other team members.
*   **XP Share:** When a player in a team gains XP, a portion of that XP is shared with other team members.
*   **Potion Share:** When a player in a team drinks a potion, the effects are shared with other team members.
*   **Death Debuffs:** When a player in a team dies, the other team members receive a debuff.

## Commands

*   `/cohere` or `/ch`: The main command for the Cohere plugin.
    *   `help`: Displays the help message.
    *   `reload`: Reloads the plugin's configuration.
*   `/team`: The main command for team-related actions.
    *   `create <name>`: Creates a new team.
    *   `disband`: Disbands your current team.
    *   `rename <newname>`: Renames your team.
    *   `invite <player>`: Invites a player to your team.
    *   `kick <player>`: Kicks a player from your team.
    *   `particle <id>`: Sets your team's particle effect.
    *   `leave`: Leaves your current team.
    *   `transferownership <player>`: Transfers ownership of your team to another player.
    *   `accept`: Accepts a team invite.
    *   `deny`: Denies a team invite.

## Permissions

*   `cohere.reload`: Allows reloading the plugin's configuration. (Default: op)

## Configuration

The `config.yml` file contains the following options:

*   `coherences.painshare.enabled`: Enables or disables the pain share feature.
*   `coherences.potionshare.enabled`: Enables or disables the potion share feature.
*   `coherences.xpshare.enabled`: Enables or disables the XP share feature.
*   `coherences.deathdebuffs.enabled`: Enables or disables the death debuffs feature.
*   `permission-nodes.enabled`: Enables or disables permission checks for team commands.
*   `disabled-worlds`: A list of worlds where the plugin's features are disabled.

The `Teams.yml` file stores all the team data.

The `invites.yml` file stores all the pending team invites.
