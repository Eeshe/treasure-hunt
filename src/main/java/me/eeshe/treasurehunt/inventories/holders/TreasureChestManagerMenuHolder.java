package me.eeshe.treasurehunt.inventories.holders;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public record TreasureChestManagerMenuHolder(int currentPage)implements InventoryHolder{

@Override
public @NotNull Inventory getInventory(){
        return null;
        }
        }
