package me.eeshe.treasurehunt.models.config;

import me.eeshe.treasurehunt.TreasureHunt;
import me.eeshe.penpenlib.files.config.ConfigWrapper;
import me.eeshe.penpenlib.models.config.PenMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message extends PenMessage {
    private static final List<PenMessage> MESSAGES = new ArrayList<>();
    private static final ConfigWrapper CONFIG_WRAPPER = new ConfigWrapper(TreasureHunt.getInstance(), null, "messages.yml");
    private static final Map<String, PenMessage> PLACEHOLDERS = new HashMap<>();

    public static final Message HELP_COMMAND_INFO = new Message("help-command-info", "Displays this list.");
    public static final Message HELP_COMMAND_USAGE = new Message("help-command-usage", "/treasurehunt help");

    public static final Message RELOAD_COMMAND_INFO = new Message("reload-command-info", "Reloads the plugin's configuration file.");
    public static final Message RELOAD_COMMAND_USAGE = new Message("reload-command-usage", "/treasurehunt reload");

    public static final Message ADD_CHEST_COMMAND_INFO = new Message("add-chest-command-info", "Adds the chest you are looking at as a treasure chest.");
    public static final Message ADD_CHEST_COMMAND_USAGE = new Message("add-chest-command-usage", "/treasurehunt addchest <TreasureChestID>");
    public static final Message NOT_LOOKING_AT_CHEST = new Message("not-looking-at-chest", "&cYou are not looking at a chest.");
    public static final Message ALREADY_TREASURE_CHEST = new Message("already-treasure-chest", "&cThis chest is already a treasure chest under ID &l%id%&c.");
    public static final Message ALREADY_USED_ID = new Message("already-used-id", "&cID &l%id%&c is already used by another treasure chest.");
    public static final Message ADD_CHEST_COMMAND_SUCCESS = new Message("add-chest-command-success", "&aTreasure chest &l%id%&a successfully added. Edit it with &l/treasurehunt manager&a.");

    public static final Message REMOVE_CHEST_COMMAND_INFO = new Message("remove-chest-command-info", "Removes the specified treasure chest.");
    public static final Message REMOVE_CHEST_COMMAND_USAGE = new Message("remove-chest-command-usage", "/treasurehunt removechest <TreasureChest>");
    public static final Message TREASURE_CHEST_NOT_FOUND = new Message("treasure-chest-not-found", "&cUnknown treasure chest &l%id%&c.");
    public static final Message REMOVE_CHEST_COMMAND_SUCCESS = new Message("remove-chest-command-success", "&aSuccessfully removed treasure chest &l%id%&a.");

    public static final Message MANAGER_COMMAND_INFO = new Message("manager-command-info", "Opens the treasure chest manager menu.");
    public static final Message MANAGER_COMMAND_USAGE = new Message("manager-command-usage", "/treasurehunt manager");

    public static final Message START_COMMAND_INFO = new Message("start-command-info", "Starts a treasure hunt for the specified treasure chest.");
    public static final Message START_COMMAND_USAGE = new Message("start-command-usage", "/treasurehunt start <TreasureChest>");

    public static final Message STOP_COMMAND_INFO = new Message("stop-command-info", "Stops the treasure hunt for the specified treasure chest.");
    public static final Message STOP_COMMAND_USAGE = new Message("stop-command-usage", "/treasurehunt stop <TreasureChest>");

    public static final Message GET_CLUE_COMMAND_INFO = new Message("get-clue-command-info", "Gives you a clue for the current treasure hunt.");
    public static final Message GET_CLUE_COMMAND_USAGE = new Message("get-clue-command-usage", "/treasurehunt getclue");

    public static final Message LEADERBOARD_COMMAND_INFO = new Message("leaderboard-command-info", "Opens the treasure hunter leaderboard.");
    public static final Message LEADERBOARD_COMMAND_USAGE = new Message("leaderboard-command-usage", "/treasurehunt leaderboard");
    public static final Message FETCHING_LEADERBOARD = new Message("fetching-leaderboard", "&eFetching leaderboard...");

    public static final Message TREASURE_ITEM_EDIT_INSTRUCTIONS = new Message("treasure-item-edit-instructions", "&eEnter the new chance of this item as a chat message.\nType &ocancel&e to cancel this.");
    public static final Message TREASURE_ITEM_EDIT_CANCEL = new Message("treasure-item-edit-cancel", "&cOperation cancelled.");
    public static final Message TREASURE_ITEM_EDIT_INVALID_CHANCE = new Message("treasure-item-edit-invalid-chance", "&cChance must be between 1 and 100");

    public static final Message TREASURE_CHEST_DELETE_CONFIRMATION = new Message("treasure-chest-delete-confirmation", "&eClick again to confirm the treasure chest deletion.");

    public static final Message TREASURE_HUNT_ALREADY_RUNNING = new Message("treasure-hunt-already-running", "&cThere is already a treasure hunt in progress.");
    public static final Message TREASURE_CHEST_MISSING_CHEST = new Message("treasure-chest-missing-chest", "&cThis treasure chest doesn't have a chest in its location.");
    public static final Message NO_TREASURE_PERMISSION = new Message("no-treasure-permission", "&cYou don't have the permissions to open treasure chests.");
    public static final Message TREASURE_HUNT_END = new Message("treasure-hunt-end", "&b&l%player%&b found the treasure hunt chest!");

    public static final Message TREASURE_HUNT_START = new Message("treasure-hunt-start", "&eA treasure chest hunt has started. Run &l/treasurehunt getclue&e to get a clue to find it!");
    public static final Message NO_TREASURE_HUNT_RUNNING = new Message("no-treasure-hunt-running", "&cThere is no treasure hunt running.");
    public static final Message INCORRECT_TREASURE_CHEST = new Message("incorrect-treasure-chest", "&cThe current treasure hunt doesn't target this treasure.");
    public static final Message TREASURE_HUNT_CANCEL = new Message("treasure-hunt-cancel", "&cThe treasure hunt has been cancelled.");

    public static final Message TREASURE_CHEST_REMOVE_INSTRUCTIONS = new Message("treasure-chest-remove-instructions", "&cThis chest is a treasure chest. Type &l/treasurehunt removechest %id%&c to remove it.");

    public Message(String path, String defaultValue) {
        super(path, defaultValue);
    }

    public Message() {
    }

    @Override
    public List<PenMessage> getMessages() {
        return MESSAGES;
    }

    @Override
    public ConfigWrapper getConfigWrapper() {
        return CONFIG_WRAPPER;
    }

    @Override
    public Map<String, PenMessage> getPlaceholders() {
        return PLACEHOLDERS;
    }
}
