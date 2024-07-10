package me.eeshe.treasurehunt.inventories;

import me.eeshe.penpenlib.models.config.ConfigMenu;
import me.eeshe.penpenlib.models.config.MenuItem;
import me.eeshe.penpenlib.util.ItemUtil;
import me.eeshe.penpenlib.util.MenuUtil;
import me.eeshe.penpenlib.util.StringUtil;
import me.eeshe.treasurehunt.models.TreasureHunter;
import me.eeshe.treasurehunt.models.config.Menu;
import me.eeshe.treasurehunt.inventories.holders.LeaderboardMenuHolder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class LeaderboardMenu {

    /**
     * Creates and returns an inventory containing the LeaderboardMenu.
     *
     * @param creator         Player creating the menu.
     * @param treasureHunters Ranking of treasure hunters.
     * @return Inventory containing the LeaderboardMenu.
     */
    public static Inventory create(Player creator, Map<UUID, TreasureHunter> treasureHunters) {
        ConfigMenu configMenu = Menu.HUNTER_LEADERBOARD.fetch();
        // Attempt to set the player-stats menu item to the player's skull skin
        ListIterator<MenuItem> menuItemsIterator = configMenu.getMenuItems().listIterator();
        while (menuItemsIterator.hasNext()) {
            MenuItem menuItem = menuItemsIterator.next();
            if (!menuItem.getId().equals("player-stats")) continue;

            ItemStack item = menuItem.getItem();
            if (!(item.getItemMeta() instanceof SkullMeta meta)) break; // No other items can be player-stats, stop loop

            meta.setOwningPlayer(creator);
            item.setItemMeta(meta);

            menuItemsIterator.set(new MenuItem(menuItem.getId(), item, menuItem.getSlot()));
        }

        TreasureHunter treasureHunter = TreasureHunter.fromPlayer(creator);
        int position = new ArrayList<>(treasureHunters.keySet()).indexOf(creator.getUniqueId()) + 1;
        Inventory inventory = configMenu.createInventory(new LeaderboardMenuHolder(), true, false,
                false, true, Map.ofEntries(
                        Map.entry("%position%", String.valueOf(position)),
                        Map.entry("%points%", StringUtil.formatNumber(treasureHunter.getHuntedTreasuresAmount()))
                )
        );
        addLeaderboardItems(inventory, treasureHunters);
        MenuUtil.placeFillerItems(configMenu, inventory);
        return inventory;
    }

    /**
     * Adds the passed treasure hunters to the passed inventory.
     *
     * @param inventory       The inventory to add the treasure hunters to.
     * @param treasureHunters The treasure hunters to add.
     */
    private static void addLeaderboardItems(Inventory inventory, Map<UUID, TreasureHunter> treasureHunters) {
        int position = 1;
        List<Integer> leaderboardSlots = Menu.HUNTER_LEADERBOARD.getAdditionalConfigIntList("leaderboard-slots");
        for (TreasureHunter treasureHunter : treasureHunters.values()) {
            if (inventory.firstEmpty() == -1) break;
            if (leaderboardSlots.isEmpty()) break;

            int slot = leaderboardSlots.remove(0) - 1;
            inventory.setItem(slot, createLeaderboardItem(treasureHunter, position));
            position += 1;
        }
    }

    /**
     * Creates the ItemStack representing the passed TreasureHunter.
     *
     * @param treasureHunter The TreasureHunter to create the item for.
     * @param position       The position of the hunter in the leaderboard.
     * @return The ItemStack representing the passed TreasureHunter.
     */
    private static ItemStack createLeaderboardItem(TreasureHunter treasureHunter, int position) {
        OfflinePlayer offlinePlayer = treasureHunter.getOfflinePlayer();
        String name = Menu.HUNTER_LEADERBOARD.getAdditionalConfigString("top-item.name")
                .replace("%player_name%", offlinePlayer.getName())
                .replace("%position%", String.valueOf(position));
        List<String> lore = Menu.HUNTER_LEADERBOARD.getAdditionalConfigStringList("top-item.lore");
        lore.replaceAll(string -> {
            string = string.replace("%position%", String.valueOf(position));
            string = string.replace("%points%", StringUtil.formatNumber(treasureHunter.getHuntedTreasuresAmount()));

            return string;
        });

        ItemStack item = ItemUtil.generateItemStack(Material.PLAYER_HEAD, name, lore);
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();

        skullMeta.setOwningPlayer(treasureHunter.getOfflinePlayer());
        item.setItemMeta(skullMeta);

        return item;
    }
}
