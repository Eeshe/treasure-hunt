package me.eeshe.treasurehunt.models.config;

import me.eeshe.penpenlib.files.config.ConfigWrapper;
import me.eeshe.penpenlib.models.config.MenuItem;
import me.eeshe.penpenlib.models.config.PenMenu;
import me.eeshe.penpenlib.util.ItemUtil;
import me.eeshe.treasurehunt.TreasureHunt;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Menu extends PenMenu {
    private static final List<PenMenu> MENUS = new ArrayList<>();
    private static final ConfigWrapper CONFIG_WRAPPER = new ConfigWrapper(TreasureHunt.getInstance(), null, "menus.yml");

    public static final Menu TREASURE_CHEST_MANAGER = new Menu("treasure-chest-manager", 0, "Treasure Chest Manager",
            Material.BLUE_STAINED_GLASS_PANE,
            List.of(-1),
            Material.BLACK_STAINED_GLASS_PANE,
            List.of(-1),
            new MenuItem("previous_page", ItemUtil.generateItemStack(
                    Material.ARROW,
                    "&7Previous Page"
            ), -9),
            new MenuItem("next_page", ItemUtil.generateItemStack(
                    Material.ARROW,
                    "&7Next Page"
            ), -1),
            null,
            List.of(
                    new MenuItem("create-instructions", ItemUtil.generateItemStack(
                            Material.COMPASS,
                            "&eCreate Instructions",
                            "&7Run &l/treasurehunt addchest",
                            "&7while looking at a chest."
                    ), -5)
            ),
            Map.ofEntries(
                    Map.entry("treasure-chest-item", Map.ofEntries(
                            Map.entry("item", Material.CHEST.name()),
                            Map.entry("name", "&7%id%"),
                            Map.entry("lore", List.of(
                                    "&9X: &e%x%",
                                    "&9Y: &e%y%",
                                    "&9Z: &e%z%",
                                    "&7Shift + Left-Click to teleport"
                            ))
                    ))
            )
    );

    public static final Menu TREASURE_CHEST_SETTINGS = new Menu("treasure-chest-settings", 0, "%id% Settings",
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            List.of(-1),
            Material.BLACK_STAINED_GLASS_PANE,
            List.of(-1),
            new MenuItem("previous_page", ItemUtil.generateItemStack(
                    Material.ARROW,
                    "&7Previous Page"
            ), -9),
            new MenuItem("next_page", ItemUtil.generateItemStack(
                    Material.ARROW,
                    "&7Next Page"
            ), -1),
            new MenuItem("back", ItemUtil.generateItemStack(
                    Material.RED_STAINED_GLASS_PANE,
                    "&cBack"
            ), 1),
            List.of(
                    new MenuItem("reward-instructions", ItemUtil.generateItemStack(
                            Material.COMPASS,
                            "&eAdding Rewards",
                            "&7Click items in your inventory to add them as rewards"
                    ), 5),
                    new MenuItem("start-hunt", ItemUtil.generateItemStack(
                            Material.LIME_CONCRETE,
                            "&aStart Hunt",
                            "&7Starts a treasure hunt for this chest."
                    ), -6),
                    new MenuItem("delete", ItemUtil.generateItemStack(
                            Material.BARRIER,
                            "&cDelete",
                            "&7Deletes this treasure chest."
                    ), -5),
                    new MenuItem("stop-hunt", ItemUtil.generateItemStack(
                            Material.RED_CONCRETE,
                            "&cStop Hunt",
                            "&7Stops this treasure chest's hunt."
                    ), -4)
            ),
            Map.ofEntries(
                    Map.entry("reward-item", Map.ofEntries(
                            Map.entry("lore", List.of(
                                    "&3Amount: &e%amount%",
                                    "&3Chance: &e%chance%%",
                                    "",
                                    "&7Left-Click to increase amount.",
                                    "&7Right-Click to decrease amount.",
                                    "&7Shift + Left-Click to modify chance.",
                                    "&7Shift + Right-Click to remove."
                            ))
                    ))
            )
    );

    public static final Menu HUNTER_LEADERBOARD = new Menu("hunter-leaderboard", 54, "Treasure Hunter Leaderboard",
            Material.YELLOW_STAINED_GLASS_PANE,
            List.of(-1),
            Material.BLUE_STAINED_GLASS_PANE,
            List.of(-1),
            null,
            null,
            null,
            List.of(
                    new MenuItem("player-stats", ItemUtil.generateItemStack(
                            Material.PLAYER_HEAD,
                            "&eYour Stats",
                            "&7Position: &b#%position%",
                            "&7Points: &b%points%"
                    ), -5)
            ),
            Map.ofEntries(
                    Map.entry("top-item", Map.ofEntries(
                            Map.entry("name", "&9#%position% &f- &e%player_name%"),
                            Map.entry("lore", List.of(
                                    "&7Points: &b%points%"
                            ))
                    )),
                    Map.entry("leaderboard-slots", List.of(
                            14, 22, 24, 30, 32, 34, 38, 40, 42
                    ))
            )
    );

    public Menu(String path, int defaultSize, String defaultTitle, Material defaultFrame,
                List<Integer> defaultFrameSlots, Material defaultFiller, List<Integer> defaultFillerSlots,
                MenuItem defaultPreviousPageItem, MenuItem defaultNextPageItem, MenuItem defaultBackItem,
                List<MenuItem> defaultMenuItems, Map<String, Object> additionalConfigs) {
        super(path, defaultSize, defaultTitle, defaultFrame, defaultFrameSlots, defaultFiller, defaultFillerSlots,
                defaultPreviousPageItem, defaultNextPageItem, defaultBackItem, defaultMenuItems, additionalConfigs);
    }

    public Menu() {
    }

    @Override
    public List<PenMenu> getMenus() {
        return MENUS;
    }

    @Override
    public ConfigWrapper getConfigWrapper() {
        return CONFIG_WRAPPER;
    }
}
