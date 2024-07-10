package me.eeshe.treasurehunt.models.config;

import me.eeshe.treasurehunt.TreasureHunt;
import me.eeshe.penpenlib.files.config.ConfigWrapper;
import me.eeshe.penpenlib.models.config.PenSound;

import java.util.ArrayList;
import java.util.List;

public class Sound extends PenSound {
    private static final List<PenSound> SOUNDS = new ArrayList<>();
    private static final ConfigWrapper CONFIG_WRAPPER = new ConfigWrapper(TreasureHunt.getInstance(), null, "sounds.yml");

    public static final Sound TREASURE_CHEST_CREATE = new Sound("treasure-chest-create", true, org.bukkit.Sound.BLOCK_BARREL_OPEN, 1.0f, 1.2f);

    public static final Sound GET_CLUE = new Sound("get-clue", true, org.bukkit.Sound.ENTITY_VILLAGER_WORK_CARTOGRAPHER, 1.0f, 1.2f);

    public static final Sound TREASURE_CHEST_MANAGER_MENU_OPEN = new Sound("treasure-chest-manager-menu-open", true, org.bukkit.Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
    public static final Sound TREASURE_CHEST_SETTINGS_MENU_OPEN = new Sound("treasure-chest-settings-menu-open", true, org.bukkit.Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
    public static final Sound LEADERBOARD_MENU_OPEN = new Sound("leaderboard-menu-open", true, org.bukkit.Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

    public static final Sound TREASURE_ITEM_ADD = new Sound("treasure-item-add", true, org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.1f);
    public static final Sound TREASURE_ITEM_REMOVE = new Sound("treasure-item-remove", true, org.bukkit.Sound.BLOCK_GRAVEL_BREAK, 1.0f, 1.5f);
    public static final Sound TREASURE_ITEM_AMOUNT_INCREASE = new Sound("treasure-item-amount-increase", true, org.bukkit.Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1.0f, 1.5f);
    public static final Sound TREASURE_ITEM_AMOUNT_DECREASE = new Sound("treasure-item-amount-decrease", true, org.bukkit.Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1.0f, 1.0f);


    public static final Sound TREASURE_CHEST_DELETE = new Sound("treasure-chest-delete", true, org.bukkit.Sound.BLOCK_GRAVEL_BREAK, 1.0f, 1.5f);
    public static final Sound TREASURE_HUNT_START = new Sound("treasure-hunt-start", true, org.bukkit.Sound.ITEM_GOAT_HORN_SOUND_1, 2.0f, 1.1f);
    public static final Sound TREASURE_HUNT_END = new Sound("treasure-hunt-end", true, org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
    public static final Sound TREASURE_HUNT_CANCEL = new Sound("treasure-hunt-cancel", true, org.bukkit.Sound.BLOCK_NOTE_BLOCK_FLUTE, 1.0f, 0.5f);

    public static final Sound LEADERBOARD_FETCH = new Sound("leaderboard-fetch", true, org.bukkit.Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1.0f, 1.2f);

    public Sound(String path, boolean defaultEnabled, org.bukkit.Sound defaultSound, float defaultVolume, float defaultPitch) {
        super(path, defaultEnabled, defaultSound, defaultVolume, defaultPitch);
    }

    public Sound() {
    }

    @Override
    public List<PenSound> getSounds() {
        return SOUNDS;
    }

    @Override
    public ConfigWrapper getConfigWrapper() {
        return CONFIG_WRAPPER;
    }
}
