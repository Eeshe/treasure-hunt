package me.eeshe.treasurehunt.models;

import me.eeshe.treasurehunt.TreasureHunt;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class TreasureHunter {
    private final UUID uuid;
    private int huntedTreasuresAmount;

    public TreasureHunter(OfflinePlayer player) {
        this.uuid = player.getUniqueId();
        this.huntedTreasuresAmount = 0;
    }

    public TreasureHunter(UUID uuid, int huntedTreasuresAmount) {
        this.uuid = uuid;
        this.huntedTreasuresAmount = huntedTreasuresAmount;
    }

    /**
     * Searches and returns the TreasureHunter corresponding to the passed player.
     *
     * @param player The player to search for.
     * @return The TreasureHunter corresponding to the passed player.
     */
    public static TreasureHunter fromPlayer(OfflinePlayer player) {
        return TreasureHunt.getInstance().getTreasureHunterManager().getTreasureHunters().get(player.getUniqueId());
    }

    /**
     * Loads the TreasureHunter to the TreasureHunterManager class.
     */
    public void load() {
        TreasureHunt.getInstance().getTreasureHunterManager().getTreasureHunters().put(uuid, this);
    }

    /**
     * Unloads the TreasureHunter from the TreasureHunterManager class.
     */
    public void unload() {
        TreasureHunt.getInstance().getTreasureHunterManager().getTreasureHunters().remove(uuid);
    }

    /**
     * Saves the TreasureHunter in the SQL database.
     */
    private void saveData() {
        TreasureHunt.getInstance().getTreasureHunterManager().saveAsync(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public int getHuntedTreasuresAmount() {
        return huntedTreasuresAmount;
    }

    public void increaseHuntedTreasures() {
        setHuntedTreasuresAmount(getHuntedTreasuresAmount() + 1);
    }

    public void setHuntedTreasuresAmount(int huntedTreasuresAmount) {
        this.huntedTreasuresAmount = huntedTreasuresAmount;
        saveData();
    }
}
