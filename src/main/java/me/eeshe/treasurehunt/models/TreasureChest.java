package me.eeshe.treasurehunt.models;

import me.eeshe.penpenlib.models.config.ConfigMenu;
import me.eeshe.penpenlib.util.ItemUtil;
import me.eeshe.penpenlib.util.MenuUtil;
import me.eeshe.treasurehunt.TreasureHunt;
import me.eeshe.treasurehunt.inventories.holders.TreasureChestSettingsMenuHolder;
import me.eeshe.treasurehunt.models.config.Menu;
import me.eeshe.treasurehunt.util.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class TreasureChest {
    private static final NamespacedKey TREASURE_CHEST_KEY = new NamespacedKey(TreasureHunt.getInstance(), "treasure_chest");

    private final String id;
    private final Location location;
    private final Map<UUID, TreasureItem> treasureItems;

    public TreasureChest(String id, Location location) {
        this.id = id;
        this.location = location;
        this.treasureItems = new HashMap<>();
    }

    public TreasureChest(String id, Location location, Map<UUID, TreasureItem> rewards) {
        this.id = id;
        this.location = location;
        this.treasureItems = rewards;
    }

    /**
     * Searches the TreasureChest corresponding to the passed chest.
     *
     * @param chest The chest to search.
     * @return The TreasureChest corresponding to the passed chest.
     */
    public static TreasureChest fromChest(Chest chest) {
        return fromPersistentDataContainer(chest.getPersistentDataContainer());
    }

    /**
     * Searches and returns the TreasureChest corresponding to the passed itemStack's persistent data container.
     *
     * @param itemStack The itemStack to search.
     * @return The TreasureChest corresponding to the passed itemStack's persistent data container.
     */
    public static TreasureChest fromItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemMeta() == null) return null;

        return fromPersistentDataContainer(itemStack.getItemMeta().getPersistentDataContainer());
    }

    private static TreasureChest fromPersistentDataContainer(PersistentDataContainer persistentDataContainer) {
        return fromId(persistentDataContainer.getOrDefault(TREASURE_CHEST_KEY, PersistentDataType.STRING, ""));
    }

    /**
     * Searches and returns the TreasureChest corresponding to the passed ID.
     *
     * @param id The ID of the TreasureChest.
     * @return The TreasureChest corresponding to the passed ID.
     */
    public static TreasureChest fromId(String id) {
        return TreasureHunt.getInstance().getTreasureChestManager().getTreasureChests().get(id);
    }

    /**
     * Loads the TreasureChest and saves its data.
     */
    public void register() {
        load();
        saveData();
    }

    /**
     * Loads the TreasureChest to the TreasureChestManager class and adds the TreasureChest key to the chest in the
     * stored location.
     */
    public void load() {
        TreasureHunt.getInstance().getTreasureChestManager().getTreasureChests().put(id, this);
        Chest chest = getChest();
        if (chest == null) return;

        // Add the PersistentDataContainer key to later retrieve the TreasureChest in the fromChest function.
        chest.getPersistentDataContainer().set(TREASURE_CHEST_KEY, PersistentDataType.STRING, id);
        chest.update();
    }

    /**
     * Unloads the TreasureChest and deletes its data.
     */
    public void unregister() {
        unload();
        TreasureHunt.getInstance().getTreasureChestManager().stopHunt();
        TreasureHunt.getInstance().getTreasureChestManager().deleteAsync(this);
        // Close any treasure chest inventories with the treasure chest
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!(online.getOpenInventory().getTopInventory().getHolder() instanceof TreasureChestSettingsMenuHolder menuHolder))
                continue;
            if (!menuHolder.treasureChest().equals(this)) continue;

            online.closeInventory();
        }
        Chest chest = getChest();
        if (chest == null) return;

        chest.getPersistentDataContainer().remove(TREASURE_CHEST_KEY);
        chest.update();
    }

    /**
     * Unloads the TreasureChest from the TreasureChestManager class.
     */
    public void unload() {
        TreasureHunt.getInstance().getTreasureChestManager().getTreasureChests().remove(id);
    }

    /**
     * Saves the TreasureChest's data.
     */
    public void saveData() {
//        if (async) {
        TreasureHunt.getInstance().getTreasureChestManager().saveAsync(this);
//        } else {
//            TreasureHunt.getInstance().getTreasureChestManager().save(this);
//        }
    }

    /**
     * Creates and return an inventory with the TreasureChestSettings menu.
     *
     * @param page Page the menu will be opened in.
     * @return Inventory with the TreasureChestSettings menu.
     */
    public Inventory createSettingsMenu(int page) {
        ConfigMenu configMenu = Menu.TREASURE_CHEST_SETTINGS.fetch();
        int itemsSize = Math.max(1, treasureItems.size()); // Ensure at least 1 item slot to avoid a 2 row inventory
        Inventory inventory = configMenu.createInventory(new TreasureChestSettingsMenuHolder(this, page),
                itemsSize, page, true, false, true, true,
                Map.of("%id%", id));

        addRewardItems(inventory, configMenu, page);
        MenuUtil.placeFillerItems(configMenu, inventory);

        return inventory;
    }

    /**
     * Adds the reward items to the passed inventory.
     *
     * @param inventory  The inventory to add the reward items to.
     * @param configMenu The config menu.
     * @param page       Page the menu will be opened in.
     */
    private void addRewardItems(Inventory inventory, ConfigMenu configMenu, int page) {
        boolean hasNextPage = false;
        List<TreasureItem> treasureItems = new ArrayList<>(this.treasureItems.values());
        for (int index = MenuUtil.computeInitialIndex(configMenu, page); index < treasureItems.size(); index++) {
            if (inventory.firstEmpty() == -1) {
                hasNextPage = true;
                break;
            }
            inventory.addItem(treasureItems.get(index).generateItemStack());
        }
        MenuUtil.addPageItems(inventory, configMenu, page, hasNextPage);
    }

    /**
     * Refills the TreasureChest with its configured rewards.
     */
    public void refill() {
        Chest chest = getChest();
        if (chest == null) return;

        Inventory inventory = chest.getInventory();
        inventory.clear();
        List<Integer> emptySlots = new ArrayList<>(IntStream.range(0, inventory.getSize()).boxed().toList());
        Collections.shuffle(emptySlots);
        Random random = ThreadLocalRandom.current();
        // Once cleared, add the reward items to the chest in random slots, making sure they don't overwrite other items
        for (Map.Entry<UUID, TreasureItem> entry : treasureItems.entrySet()) {
            if (emptySlots.isEmpty()) break;
            TreasureItem treasureItem = entry.getValue();
            if (random.nextDouble(100) > treasureItem.getChance()) continue;
            int slot = emptySlots.remove(0);
            ItemStack slotItem = inventory.getItem(slot);
            if (slotItem != null && !slotItem.getType().isAir()) continue;

            inventory.setItem(slot, treasureItem.getItemStack().clone());
        }
    }

    /**
     * Generates the clue map ItemStack.
     *
     * @return The clue map ItemStack.
     */
    public ItemStack generateClueMap() {
        ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();

        mapMeta.setMapView(createMapView());
        mapMeta.addItemFlags(ItemFlag.values());
        mapItem.setItemMeta(mapMeta);

        return mapItem;
    }

    /**
     * Creates the MapView containing the treasure chest clue.
     *
     * @return MapView containing the treasure chest clue.
     */
    private MapView createMapView() {
        MapView mapView = Bukkit.createMap(location.getWorld());
        mapView.setScale(MapView.Scale.CLOSE);

        ThreadLocalRandom random = ThreadLocalRandom.current();
        mapView.setCenterX(location.getBlockX() + random.nextInt(-128, 128));
        mapView.setCenterZ(location.getBlockZ() + random.nextInt(-128, 128));

        byte xOffset = (byte) ((location.getBlockX() - mapView.getCenterX()));
        byte zOffset = (byte) ((location.getBlockZ() - mapView.getCenterZ()));
        mapView.addRenderer(new ClueRenderer(xOffset, zOffset));
        mapView.setTrackingPosition(true);
        mapView.setUnlimitedTracking(true);

        return mapView;
    }

    /**
     * Empties the chest.
     */
    public void empty() {
        Chest chest = getChest();
        if (chest == null) return;

        chest.getInventory().clear();
    }

    /**
     * Creates the ItemStack used to display the TreasureChest in the TreasureChestManager menu.
     *
     * @return The ItemStack used to display the TreasureChest in the TreasureChestManager menu.
     */
    public ItemStack generateMenuItemStack() {
        Menu menu = Menu.TREASURE_CHEST_MANAGER;
        Material material = menu.getAdditionalConfigMaterial("treasure-chest-item.item");
        String name = menu.getAdditionalConfigString("treasure-chest-item.name")
                .replace("%id%", id);
        List<String> lore = menu.getAdditionalConfigStringList("treasure-chest-item.lore");
        lore.replaceAll(string -> {
            string = string.replace("%x%", String.valueOf(location.getBlockX()));
            string = string.replace("%y%", String.valueOf(location.getBlockY()));
            string = string.replace("%z%", String.valueOf(location.getBlockZ()));
            return string;
        });
        ItemStack item = ItemUtil.generateItemStack(material, name, lore);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        meta.getPersistentDataContainer().set(TREASURE_CHEST_KEY, PersistentDataType.STRING, id);
        item.setItemMeta(meta);

        return item;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public Location getTeleportLocation() {
        return location.clone().add(0.5, 1, 0.5);
    }

    /**
     * Returns the chest block found in the stored location.
     *
     * @return The chest block found in the stored location.
     */
    public Chest getChest() {
        if (!(location.getBlock().getState() instanceof Chest chest)) {
            LogUtil.sendWarnLog("TreasureChest '" + id + "' doesn't have a chest in its configured location.");
            return null;
        }
        return chest;
    }

    public Map<UUID, TreasureItem> getTreasureItems() {
        return treasureItems;
    }

    public TreasureItem getTreasureItem(UUID treasureItemUuid) {
        return treasureItems.get(treasureItemUuid);
    }

    public void addTreasureItem(ItemStack item) {
        TreasureItem treasureItem = new TreasureItem(item.clone());
        treasureItems.put(treasureItem.getUuid(), treasureItem);
        saveData();
    }

    public void removeTreasureItem(TreasureItem treasureItem) {
        treasureItems.remove(treasureItem.getUuid());
    }
}
