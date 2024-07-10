package me.eeshe.treasurehunt.inventories;

import me.eeshe.penpenlib.models.config.ConfigMenu;
import me.eeshe.penpenlib.util.MenuUtil;
import me.eeshe.treasurehunt.TreasureHunt;
import me.eeshe.treasurehunt.inventories.holders.TreasureChestManagerMenuHolder;
import me.eeshe.treasurehunt.models.TreasureChest;
import me.eeshe.treasurehunt.models.config.Menu;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TreasureChestManagerMenu {

    /**
     * Creates and returns an inventory with the TreasureChestManagerMenu.
     *
     * @param page Page the menu should be opened in.
     * @return Inventory with the TreasureChestManagerMenu.
     */
    public static Inventory create(int page) {
        ConfigMenu configMenu = Menu.TREASURE_CHEST_MANAGER.fetch();
        List<TreasureChest> treasureChests = new ArrayList<>(TreasureHunt.getInstance().getTreasureChestManager()
                .getTreasureChests().values());

        Inventory inventory = configMenu.createInventory(new TreasureChestManagerMenuHolder(page), treasureChests.size(),
                page, true, false, false, true, new HashMap<>());
        addTreasureChestItems(inventory, configMenu, page, treasureChests);
        MenuUtil.placeFillerItems(configMenu, inventory);

        return inventory;
    }

    private static void addTreasureChestItems(Inventory inventory, ConfigMenu configMenu, int page,
                                              List<TreasureChest> treasureChests) {
        boolean hasNextPage = false;
        for (int index = MenuUtil.computeInitialIndex(configMenu, page); index < treasureChests.size(); index++) {
            if (inventory.firstEmpty() == -1) {
                hasNextPage = true;
                break;
            }
            inventory.addItem(treasureChests.get(index).generateMenuItemStack());
        }
        MenuUtil.addPageItems(inventory, configMenu, page, hasNextPage);
    }
}
