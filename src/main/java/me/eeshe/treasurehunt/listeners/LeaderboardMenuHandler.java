package me.eeshe.treasurehunt.listeners;

import me.eeshe.treasurehunt.inventories.holders.LeaderboardMenuHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LeaderboardMenuHandler implements Listener {

    /**
     * Listens when a player clicks the LeaderboardMenu and cancels it.
     *
     * @param event InventoryClickEvent
     */
    @EventHandler
    public void onLeaderboardMenuClick(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getInventory().getHolder() instanceof LeaderboardMenuHolder)) return;

        event.setCancelled(true);
    }
}
