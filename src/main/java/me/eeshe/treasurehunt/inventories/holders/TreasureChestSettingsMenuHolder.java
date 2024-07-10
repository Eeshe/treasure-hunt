package me.eeshe.treasurehunt.inventories.holders;

import me.eeshe.treasurehunt.models.TreasureChest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public record TreasureChestSettingsMenuHolder(TreasureChest treasureChest, int currentPage) implements InventoryHolder {

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
