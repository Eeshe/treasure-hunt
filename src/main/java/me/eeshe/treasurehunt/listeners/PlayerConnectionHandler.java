package me.eeshe.treasurehunt.listeners;

import me.eeshe.treasurehunt.managers.TreasureHunterManager;
import me.eeshe.treasurehunt.models.TreasureHunter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionHandler implements Listener {
    private final TreasureHunterManager treasureHunterManager;

    public PlayerConnectionHandler(TreasureHunterManager treasureHunterManager) {
        this.treasureHunterManager = treasureHunterManager;
    }

    /**
     * Listens when a player joins the server and loads its TreasureHunter instance.
     *
     * @param event PlayerJoinEvent.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        treasureHunterManager.fetchAsync(event.getPlayer()).whenComplete((treasureHunter, throwable) -> {
            treasureHunter.load();
        });
    }

    /**
     * Listens when a player leaves the server and unloads its TreasureHunter instance.
     *
     * @param event PlayerQuitEvent.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        TreasureHunter.fromPlayer(event.getPlayer()).unload();
    }
}
