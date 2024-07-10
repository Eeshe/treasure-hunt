package me.eeshe.treasurehunt.managers;

import me.eeshe.penpenlib.managers.DataManager;
import me.eeshe.treasurehunt.database.SQLManager;
import me.eeshe.treasurehunt.models.TreasureChest;
import me.eeshe.treasurehunt.models.TreasureHunter;
import me.eeshe.treasurehunt.models.TreasureItem;
import me.eeshe.treasurehunt.models.config.Message;
import me.eeshe.treasurehunt.models.config.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TreasureChestManager extends DataManager {
    private final Map<String, TreasureChest> treasureChests = new TreeMap<>(Comparator.naturalOrder());
    private final SQLManager sqlManager;

    private TreasureChest currentlyHuntedTreasureChest;

    public TreasureChestManager(Plugin plugin, SQLManager sqlManager) {
        super(plugin);
        this.sqlManager = sqlManager;
    }

    @Override
    public void load() {
        createTables();
        for (TreasureChest treasureChest : fetchAll()) {
            treasureChest.load();
        }
        this.currentlyHuntedTreasureChest = fetchCurrentTreasureHuntChest();
    }

    /**
     * Creates all the necessary TreasureChest data tables.
     */
    private void createTables() {
        createTreasureChestsTables();
        createCurrentTreasureHuntTable();
    }

    /**
     * Creates the treasure_chests table in the database if it doesn't exist.
     */
    private void createTreasureChestsTables() {
        if (!sqlManager.isConnected()) return;
        String sql = "CREATE TABLE IF NOT EXISTS treasure_chests (" +
                "id VARCHAR(255) PRIMARY KEY," +
                "world VARCHAR(255) NOT NULL," +
                "x DOUBLE NOT NULL," +
                "y DOUBLE NOT NULL," +
                "z DOUBLE NOT NULL," +
                "treasure_items LONGTEXT" +
                ")";

        try (Connection connection = sqlManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the current_treasure_hunt table in the database if it doesn't exist.
     */
    private void createCurrentTreasureHuntTable() {
        if (!sqlManager.isConnected()) return;
        String sql = "CREATE TABLE IF NOT EXISTS current_treasure_hunt (" +
                "id VARCHAR(255) PRIMARY KEY)";

        try (Connection connection = sqlManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all treasure chests from the database asynchronously.
     *
     * @return A CompletableFuture that will complete with a list of all TreasureChest objects in the database.
     */
    public CompletableFuture<List<TreasureChest>> fetchAllAsync() {
        return CompletableFuture.supplyAsync(this::fetchAll);
    }

    /**
     * Retrieves all treasure chests from the database.
     *
     * @return A list of all TreasureChest objects in the database.
     */
    public List<TreasureChest> fetchAll() {
        if (!sqlManager.isConnected()) return new ArrayList<>();

        String sql = "SELECT * FROM treasure_chests";
        List<TreasureChest> treasureChests = new ArrayList<>();

        try (Connection connection = sqlManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String worldName = rs.getString("world");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                String treasureItemsString = rs.getString("treasure_items");

                Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
                Map<UUID, TreasureItem> treasureItems = deserializeTreasureItems(treasureItemsString);

                treasureChests.add(new TreasureChest(id, location, treasureItems));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return treasureChests;
    }

    /**
     * Fetches the currently hunted TreasureChest from the database.
     *
     * @return The currently hunted TreasureChest.
     */
    private TreasureChest fetchCurrentTreasureHuntChest() {
        if (!sqlManager.isConnected()) return null;

        String sql = "SELECT * FROM current_treasure_hunt";
        try (Connection connection = sqlManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return null;

            return TreasureChest.fromId(resultSet.getString("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deserializes a Base64 encoded string into a Map of UUID to TreasureItem.
     *
     * @param treasureItemsString The Base64 encoded string to deserialize.
     * @return A Map of UUID to TreasureItem.
     */
    private Map<UUID, TreasureItem> deserializeTreasureItems(String treasureItemsString) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(treasureItemsString));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Map<UUID, TreasureItem> treasureItems = new HashMap<>();

            int size = dataInput.readInt();

            for (int i = 0; i < size; i++) {
                UUID mapKey = (UUID) dataInput.readObject();
                UUID itemUuid = (UUID) dataInput.readObject();
                ItemStack itemStack = (ItemStack) dataInput.readObject();
                double chance = dataInput.readDouble();

                TreasureItem item = new TreasureItem(itemUuid, itemStack, chance);
                treasureItems.put(mapKey, item);
            }

            dataInput.close();
            return treasureItems;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load treasure items.", e);
        }
    }

    @Override
    public void unload() {
        for (TreasureChest treasureChest : new ArrayList<>(treasureChests.values())) {
            treasureChest.unload();
        }
    }

    /**
     * Saves the passed TreasureChest in the SQL database asynchronously.
     *
     * @param treasureChest The treasure chest to save.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    public CompletableFuture<Void> saveAsync(TreasureChest treasureChest) {
        return CompletableFuture.runAsync(() -> save(treasureChest));
    }

    /**
     * Saves the passed TreasureChest in the SQL database.
     *
     * @param treasureChest The treasure chest to save.
     */
    public void save(TreasureChest treasureChest) {
        if (!sqlManager.isConnected()) return;

        String sql = "INSERT INTO treasure_chests (id, world, x, y, z, treasure_items) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "world = VALUES(world), " +
                "x = VALUES(x), " +
                "y = VALUES(y), " +
                "z = VALUES(z), " +
                "treasure_items = VALUES(treasure_items)";

        try (Connection connection = sqlManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, treasureChest.getId());
            preparedStatement.setString(2, treasureChest.getLocation().getWorld().getName());
            preparedStatement.setDouble(3, treasureChest.getLocation().getX());
            preparedStatement.setDouble(4, treasureChest.getLocation().getY());
            preparedStatement.setDouble(5, treasureChest.getLocation().getZ());
            try {
                preparedStatement.setString(6, serializeTreasureItems(treasureChest.getTreasureItems()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Serializes a Map of UUID to TreasureItem into a Base64 encoded string.
     *
     * @param treasureItems The Map of UUID to TreasureItem to serialize.
     * @return A Base64 encoded string representing the treasure items.
     */
    private String serializeTreasureItems(Map<UUID, TreasureItem> treasureItems) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(treasureItems.size());

            for (Map.Entry<UUID, TreasureItem> entry : treasureItems.entrySet()) {
                TreasureItem item = entry.getValue();
                dataOutput.writeObject(entry.getKey());
                dataOutput.writeObject(item.getUuid());
                dataOutput.writeObject(item.getItemStack());
                dataOutput.writeDouble(item.getChance());
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save treasure items.", e);
        }
    }

    /**
     * Deletes the passed TreasureChest from the database asynchronously.
     *
     * @param treasureChest The treasure chest to be deleted.
     * @return A CompletableFuture representing the asynchronous operation.
     */
    public CompletableFuture<Void> deleteAsync(TreasureChest treasureChest) {
        return CompletableFuture.runAsync(() -> delete(treasureChest));
    }

    /**
     * Deletes the passed TreasureChest from the database.
     *
     * @param treasureChest The treasure chest to be deleted.
     */
    public void delete(TreasureChest treasureChest) {
        if (!sqlManager.isConnected()) return;
        String sql = "DELETE FROM treasure_chests WHERE id = ?";

        try (Connection connection = sqlManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, treasureChest.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a TreasureHunt with the passed TreasureChest.
     *
     * @param player        Player starting the treasure hunt.
     * @param treasureChest The TreasureChest to start the TreasureHunt with.
     */
    public void startHunt(Player player, TreasureChest treasureChest) {
        if (getCurrentlyHuntedTreasureChest() != null) {
            Message.TREASURE_HUNT_ALREADY_RUNNING.sendError(player);
            return;
        }
        if (treasureChest.getChest() == null) {
            Message.TREASURE_CHEST_MISSING_CHEST.sendError(player);
            return;
        }
        treasureChest.refill();
        setCurrentlyHuntedTreasureChestAsync(treasureChest);
        for (Player online : Bukkit.getOnlinePlayers()) {
            Message.TREASURE_HUNT_START.send(online, Sound.TREASURE_HUNT_START);
        }
    }

    /**
     * Forcefully stops the current treasure hunt.
     *
     * @param player        Player stopping the treasure hunt.
     * @param treasureChest The TreasureChest to stop the TreasureHunt with.
     */
    public void stopHunt(Player player, TreasureChest treasureChest) {
        if (getCurrentlyHuntedTreasureChest() == null) {
            Message.NO_TREASURE_HUNT_RUNNING.sendError(player);
            return;
        }
        if (!treasureChest.equals(getCurrentlyHuntedTreasureChest())) {
            Message.INCORRECT_TREASURE_CHEST.sendError(player);
            return;
        }
        setCurrentlyHuntedTreasureChest(null);
        for (Player online : Bukkit.getOnlinePlayers()) {
            Message.TREASURE_HUNT_CANCEL.send(online, Sound.TREASURE_HUNT_CANCEL);
        }
    }

    /**
     * Forcefully stops the current treasure hunt.
     */
    public void stopHunt() {
        if (getCurrentlyHuntedTreasureChest() == null) return;
        setCurrentlyHuntedTreasureChest(null);
        for (Player online : Bukkit.getOnlinePlayers()) {
            Message.TREASURE_HUNT_CANCEL.send(online, Sound.TREASURE_HUNT_CANCEL);
        }
    }

    /**
     * Ends the current treasure hunt.
     *
     * @param winner Winner of the treasure hunt.
     */
    public void endHunt(Player winner) {
        TreasureHunter.fromPlayer(winner).increaseHuntedTreasures();
        for (Player online : Bukkit.getOnlinePlayers()) {
            Message.TREASURE_HUNT_END.send(online, Sound.TREASURE_HUNT_END, Map.of("%player%", winner.getName()));
        }
        setCurrentlyHuntedTreasureChestAsync(null);
    }

    /**
     * Sets the currently hunted TreasureChest in the database table asynchronously.
     *
     * @param treasureChest The currently hunted TreasureChest.
     */
    private CompletableFuture<Void> setCurrentlyHuntedTreasureChestAsync(TreasureChest treasureChest) {
        return CompletableFuture.runAsync(() -> setCurrentlyHuntedTreasureChest(treasureChest));
    }

    /**
     * Sets the currently hunted TreasureChest in the database table.
     *
     * @param treasureChest The currently hunted TreasureChest.
     */
    private void setCurrentlyHuntedTreasureChest(TreasureChest treasureChest) {
        this.currentlyHuntedTreasureChest = treasureChest;
        if (!sqlManager.isConnected()) return;

        String sql;
        if (treasureChest == null) {
            sql = "DELETE FROM current_treasure_hunt";
        } else {
            sql = "INSERT INTO current_treasure_hunt (id) VALUES (?) ON DUPLICATE KEY UPDATE id = VALUES(id)";
        }

        try (Connection connection = sqlManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            if (treasureChest != null) {
                preparedStatement.setString(1, treasureChest.getId());
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, TreasureChest> getTreasureChests() {
        return treasureChests;
    }

    public TreasureChest getCurrentlyHuntedTreasureChest() {
        return currentlyHuntedTreasureChest;
    }
}