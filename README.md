# TreasureHunt

## Commands and Permissions

| **Command**                                 | **Description**                                           | **Permission**             |
|---------------------------------------------|-----------------------------------------------------------|----------------------------|
| `/treasurehunt`                             | Base command of the plugin.                               | `treasurehunt.base`        |
| `/treasurehunt addchest <TreasureChestID>`  | Creates a treasure chest with the specified ID.           | `treasurehunt.addchest`    |
| `/treasurehunt removechest <TreasureChest>` | Deletes the specified treasure chest.                     | `treasurehunt.removechest` |
| `/treasurehunt manager`                     | Opens the treasure chest manager menu.                    | `treasurehunt.manager`     |
| `/treasurehunt start <TreasureChest>`       | Starts a treasure hunt for the specified treasure chest.  | `treasurehunt.start`       |
| `/treasurehunt stop <TreasureChest>`        | Stops the treasure hunt for the specified treasure chest. | `treasurehunt.stop`        |
| `/treasurehunt getclue`                     | Gives you a clue for the current treasure hunt.           | `treasurehunt.getclue`     |
| `/treasurehunt leaderboard`                 | Opens the treasure hunter leaderboard.                    | `treasurehunt.leaderboard` |
| `/treasurehunt reload`                      | Reloads the configurations of the plugin.                 | `treasurehunt.reload`      |
| `/treasurehunt help`                        | Displays a list of the commands of the plugin.            | `treasurehunt.help`        |

## Configurations

| **Configuration**              | **Description**                                                      |
|--------------------------------|----------------------------------------------------------------------|
| database                       | MySQL database settings.                                             |
| treasure-chest.open-permission | Permission required for players to be able to open a treasure chest. |

Messages, menus and sounds are also configurable in their corresponding files.

## Installation

1. Download the latest version of both TreasureHunt and [PenPenLib](https://github.com/Eeshe/pen-pen-lib).
2. Place them in the `plugins` folder of your server.
3. Boot the server.
   1. During the first boot, TreasureHunt won't be enabled since it will be using the default MySQL config.
4. Open the `config.yml` and modify the database settings with your MySQL credentials.
5. Reboot the server.
6. After this, the plugin should be ready to go!