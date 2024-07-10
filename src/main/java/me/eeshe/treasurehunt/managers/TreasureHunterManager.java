package me.eeshe.treasurehunt.managers;

import me.eeshe.penpenlib.managers.DataManager;
import me.eeshe.treasurehunt.database.SQLManager;
import me.eeshe.treasurehunt.models.TreasureHunter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TreasureHunterManager extends DataManager {
    private final Map<UUID, TreasureHunter> treasureHunters = new HashMap<>();
    private final SQLManager sqlManager;

    public TreasureHunterManager(Plugin plugin, SQLManager sqlManager) {
        super(plugin);
        this.sqlManager = sqlManager;
    }

    @Override
    public void load() {
        createTable();
        for (Player online : Bukkit.getOnlinePlayers()) {
            fetch(online).load();
        }
    }

    /**
     * Creates the necessary table in the database if it doesn't exist.
     */
    public void createTable() {
        if (!sqlManager.isConnected()) return;

        String sql = "CREATE TABLE IF NOT EXISTS treasure_hunters (" +
                "uuid VARCHAR(36) PRIMARY KEY," +
                "hunted_treasures_amount INT NOT NULL" +
                ")";
        try (Connection connection = sqlManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all treasure hunters from the database asynchronously.
     *
     * @return A CompletableFuture that will complete with a list of all TreasureHunter objects in the database.
     */
    public CompletableFuture<List<TreasureHunter>> fetchAllAsync() {
        return CompletableFuture.supplyAsync(this::fetchAll);
    }

    /**
     * Retrieves all treasure hunters from the database.
     *
     * @return A list of all TreasureHunter objects in the database.
     */
    public List<TreasureHunter> fetchAll() {
        if (!sqlManager.isConnected()) return new ArrayList<>();

        String sql = "SELECT * FROM treasure_hunters";
        List<TreasureHunter> treasureHunters = new ArrayList<>();
        try (Connection connection = sqlManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                int huntedTreasuresAmount = rs.getInt("hunted_treasures_amount");

                treasureHunters.add(new TreasureHunter(uuid, huntedTreasuresAmount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return treasureHunters;
    }

    /**
     * Retrieves a TreasureHunter from the database by UUID asynchronously.
     *
     * @param player Player to retrieve.
     * @return A CompletableFuture that will complete with the TreasureHunter object if found, or null otherwise.
     */
    public CompletableFuture<TreasureHunter> fetchAsync(OfflinePlayer player) {
        return CompletableFuture.supplyAsync(() -> fetch(player));
    }

    /**
     * Retrieves a TreasureHunter from the database by UUID.
     *
     * @param player Player to retrieve.
     * @return The TreasureHunter object if found, null otherwise.
     */
    public TreasureHunter fetch(OfflinePlayer player) {
        if (!sqlManager.isConnected()) return null;

        String sql = "SELECT * FROM treasure_hunters WHERE uuid = ?";
        UUID uuid = player.getUniqueId();
        try (Connection connection = sqlManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, uuid.toString());
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int huntedTreasuresAmount = rs.getInt("hunted_treasures_amount");
                    return new TreasureHunter(uuid, huntedTreasuresAmount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new TreasureHunter(player);
    }

    @Override
    public void unload() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            TreasureHunter.fromPlayer(online).unload();
        }
    }

    /**
     * Saves the passed TreasureHunter in the SQL database asynchronously.
     *
     * @param treasureHunter The treasure hunter to save.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    public CompletableFuture<Void> saveAsync(TreasureHunter treasureHunter) {
        return CompletableFuture.runAsync(() -> save(treasureHunter));
    }

    /**
     * Saves the passed TreasureHunter in the SQL database.
     *
     * @param treasureHunter The treasure hunter to save.
     */
    public void save(TreasureHunter treasureHunter) {
        if (!sqlManager.isConnected()) return;
        String sql = "INSERT INTO treasure_hunters (uuid, hunted_treasures_amount) " +
                "VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "hunted_treasures_amount = VALUES(hunted_treasures_amount)";

        try (Connection connection = sqlManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, treasureHunter.getUuid().toString());
            preparedStatement.setInt(2, treasureHunter.getHuntedTreasuresAmount());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, TreasureHunter> getTreasureHunters() {
        return treasureHunters;
    }
}