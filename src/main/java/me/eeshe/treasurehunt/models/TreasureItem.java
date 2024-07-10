package me.eeshe.treasurehunt.models;

import me.eeshe.penpenlib.util.StringUtil;
import me.eeshe.treasurehunt.TreasureHunt;
import me.eeshe.treasurehunt.models.config.Menu;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class TreasureItem {
    private static final NamespacedKey TREASURE_ITEM_KEY = new NamespacedKey(TreasureHunt.getInstance(), "treasure_item");
    private final UUID uuid;
    private final ItemStack itemStack;
    private double chance;

    public TreasureItem(ItemStack itemStack) {
        this.uuid = UUID.randomUUID();
        this.itemStack = itemStack;
        this.chance = 100;
    }

    public TreasureItem(UUID uuid, ItemStack itemStack, double chance) {
        this.uuid = uuid;
        this.itemStack = itemStack;
        this.chance = chance;
    }

    /**
     * Returns the TreasureItem UUID stored in the passed item's persistent data container.
     *
     * @param item Item to search.
     * @return The TreasureItem UUID stored in the passed item's persistent data container.
     */
    public static UUID getUuid(ItemStack item) {
        if (item == null || item.getItemMeta() == null) return null;
        PersistentDataContainer persistentDataContainer = item.getItemMeta().getPersistentDataContainer();
        if (!persistentDataContainer.has(TREASURE_ITEM_KEY, PersistentDataType.STRING)) return null;

        return UUID.fromString(persistentDataContainer.get(TREASURE_ITEM_KEY, PersistentDataType.STRING));
    }

    /**
     * Generates an ItemStack from the TreasureItem.
     *
     * @return The generated ItemStack.
     */
    public ItemStack generateItemStack() {
        Menu menu = Menu.TREASURE_CHEST_SETTINGS;
        ItemStack item = itemStack.clone();
        ItemMeta meta = item.getItemMeta();

        List<String> lore = menu.getAdditionalConfigStringList("reward-item.lore");
        lore.replaceAll(string -> {
            string = string.replace("%amount%", String.valueOf(item.getAmount()));
            string = string.replace("%chance%", StringUtil.formatNumber(chance));

            return StringUtil.formatColor(string);
        });
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(TREASURE_ITEM_KEY, PersistentDataType.STRING, uuid.toString());

        item.setItemMeta(meta);
        return item;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }
}
