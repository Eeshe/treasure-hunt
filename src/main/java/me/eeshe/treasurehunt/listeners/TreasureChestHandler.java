package me.eeshe.treasurehunt.listeners;

import me.eeshe.penpenlib.models.Scheduler;
import me.eeshe.penpenlib.models.config.CommonSound;
import me.eeshe.penpenlib.models.config.MenuItem;
import me.eeshe.penpenlib.util.InputUtil;
import me.eeshe.penpenlib.util.MenuUtil;
import me.eeshe.penpenlib.util.StringUtil;
import me.eeshe.penpenlib.util.TeleportUtil;
import me.eeshe.treasurehunt.TreasureHunt;
import me.eeshe.treasurehunt.inventories.TreasureChestManagerMenu;
import me.eeshe.treasurehunt.inventories.holders.TreasureChestManagerMenuHolder;
import me.eeshe.treasurehunt.inventories.holders.TreasureChestSettingsMenuHolder;
import me.eeshe.treasurehunt.managers.TreasureChestManager;
import me.eeshe.treasurehunt.models.TreasureChest;
import me.eeshe.treasurehunt.models.TreasureItem;
import me.eeshe.treasurehunt.models.config.Message;
import me.eeshe.treasurehunt.models.config.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TreasureChestHandler implements Listener {
    private final Map<UUID, TreasureItemEditor> treasureItemEditors = new HashMap<>();
    private final Set<UUID> treasureChestDeleters = new HashSet<>();
    private final TreasureHunt plugin;

    public TreasureChestHandler(TreasureHunt plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens when a player opens a chest and attempts to stop a treasure hunt.
     *
     * @param event PlayerInteractEvent.
     */
    @EventHandler
    public void onPlayerOpenChest(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (event.useInteractedBlock() == Event.Result.DENY) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!(event.getClickedBlock().getState() instanceof Chest chest)) return;
        TreasureChest treasureChest = TreasureChest.fromChest(chest);
        if (treasureChest == null) return;

        Player player = event.getPlayer();
        String treasureOpenPermission = plugin.getMainConfig().getTreasureChestOpenPermission();
        if (!treasureOpenPermission.isEmpty() && !player.hasPermission(treasureOpenPermission)) {
            event.setCancelled(true);
            Message.NO_TREASURE_PERMISSION.sendError(player);
            return;
        }
        TreasureChestManager treasureChestManager = plugin.getTreasureChestManager();
        if (!treasureChest.equals(treasureChestManager.getCurrentlyHuntedTreasureChest())) return;

        // Check one tick later if the chest was opened
        Scheduler.runLater(plugin, chest.getLocation(), () -> {
            if (player.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST) return;

            treasureChestManager.endHunt(player);
        }, 0L);
    }

    /**
     * Listens when a player clicks a treasure chest menu and handles it.
     *
     * @param event InventoryClickEvent
     */
    @EventHandler
    public void onTreasureChestMenuClick(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (event.getClickedInventory() == null) return;
        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (inventoryHolder instanceof TreasureChestManagerMenuHolder) {
            handleTreasureChestManagerMenu(event);
        } else if (inventoryHolder instanceof TreasureChestSettingsMenuHolder) {
            handleTreasureChestSettingsMenu(event);
        }
    }

    /**
     * Handles the clicks made in the TreasureChestManagerMenu.
     *
     * @param event InventoryClickEvent
     */
    private void handleTreasureChestManagerMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        TreasureChestManagerMenuHolder menuHolder = (TreasureChestManagerMenuHolder) event.getInventory().getHolder();
        if (MenuUtil.handleBaseMenuActions(event, null,
                () -> player.openInventory(TreasureChestManagerMenu.create(menuHolder.currentPage() - 1)),
                () -> player.openInventory(TreasureChestManagerMenu.create(menuHolder.currentPage() + 1)))) {
            return;
        }
        TreasureChest treasureChest = TreasureChest.fromItemStack(event.getCurrentItem());
        if (treasureChest == null) return;

        ClickType click = event.getClick();
        if (click.isLeftClick() && click.isShiftClick()) {
            TeleportUtil.teleport(player, treasureChest.getTeleportLocation(), true);
            player.updateInventory();
            return;
        }
        player.openInventory(treasureChest.createSettingsMenu(1));
        Sound.TREASURE_CHEST_SETTINGS_MENU_OPEN.play(player);
    }

    /**
     * Handles the clicks made in the TreasureChestSettingsMenu.
     *
     * @param event InventoryClickEvent.
     */
    private void handleTreasureChestSettingsMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        TreasureChestSettingsMenuHolder menuHolder = (TreasureChestSettingsMenuHolder) event.getInventory().getHolder();
        TreasureChest treasureChest = menuHolder.treasureChest();
        int currentPage = menuHolder.currentPage();
        if (MenuUtil.handleBaseMenuActions(event,
                () -> player.openInventory(TreasureChestManagerMenu.create(1)),
                () -> player.openInventory(treasureChest.createSettingsMenu(currentPage - 1)),
                () -> player.openInventory(treasureChest.createSettingsMenu(currentPage + 1)))) {
            return;
        }
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory.getType() == InventoryType.PLAYER) {
            handleTreasureChestSettingsPlayerInventoryClick(event);
        } else {
            handleTreasureChestSettingsTopInventoryClick(event);
        }
    }

    /**
     * Handles the clicks made in the TreasureChestSettingsMenu, specifically in the player's inventory.
     *
     * @param event InventoryClickEvent
     */
    private void handleTreasureChestSettingsPlayerInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;

        TreasureChestSettingsMenuHolder menuHolder = (TreasureChestSettingsMenuHolder) event.getInventory().getHolder();
        TreasureChest treasureChest = menuHolder.treasureChest();
        treasureChest.addTreasureItem(clickedItem);
        Sound.TREASURE_ITEM_ADD.play(player);
        player.openInventory(treasureChest.createSettingsMenu(menuHolder.currentPage()));
    }

    /**
     * Handles the clicks made in the TreasureChestSettingsMenu, specifically in the top inventory.
     *
     * @param event InventoryClickEvent
     */
    private void handleTreasureChestSettingsTopInventoryClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if (TreasureItem.getUuid(clickedItem) != null) {
            handleTreasureItemClick(event);
            return;
        }
        String menuAction = MenuItem.getMenuAction(clickedItem);
        if (menuAction == null) return;

        Player player = (Player) event.getWhoClicked();
        TreasureChest treasureChest = ((TreasureChestSettingsMenuHolder) event.getInventory().getHolder()).treasureChest();
        switch (menuAction) {
            case "start-hunt" -> plugin.getTreasureChestManager().startHunt(player, treasureChest);
            case "stop-hunt" -> plugin.getTreasureChestManager().stopHunt(player, treasureChest);
            case "delete" -> {
                if (InputUtil.askPlayerConfirmation(player, Message.TREASURE_CHEST_DELETE_CONFIRMATION, treasureChestDeleters)) {
                    return;
                }
                treasureChestDeleters.remove(player.getUniqueId());
                treasureChest.unregister();
                player.openInventory(TreasureChestManagerMenu.create(1));
                Sound.TREASURE_CHEST_DELETE.play(player);
            }
        }
    }

    /**
     * Handles the clicks made in treasure items in the TreasureChestSettingsMenu.
     *
     * @param event InventoryClickEvent
     */
    private void handleTreasureItemClick(InventoryClickEvent event) {
        TreasureChestSettingsMenuHolder menuHolder = (TreasureChestSettingsMenuHolder) event.getInventory().getHolder();
        TreasureChest treasureChest = menuHolder.treasureChest();
        TreasureItem treasureItem = treasureChest.getTreasureItem(TreasureItem.getUuid(event.getCurrentItem()));
        ItemStack item = treasureItem.getItemStack();
        Player player = (Player) event.getWhoClicked();
        ClickType click = event.getClick();
        if (!click.isShiftClick()) {
            // Player didn't shift-click, which means item amount is being changed
            int amountChange = click.isLeftClick() ? 1 : -1;
            item.setAmount(Math.max(1, Math.min(item.getMaxStackSize(), item.getAmount() + amountChange)));
            (amountChange == 1 ? Sound.TREASURE_ITEM_AMOUNT_INCREASE : Sound.TREASURE_ITEM_AMOUNT_DECREASE).play(player);
        } else {
            // Player shift-click, handle accordingly
            if (click.isRightClick()) {
                // Delete treasure item
                treasureChest.removeTreasureItem(treasureItem);
                Sound.TREASURE_ITEM_REMOVE.play(player);
            } else {
                // Change chance
                treasureItemEditors.put(player.getUniqueId(), new TreasureItemEditor(treasureChest, treasureItem));
                player.closeInventory();
                Message.TREASURE_ITEM_EDIT_INSTRUCTIONS.send(player, CommonSound.INPUT_REQUEST);
                return;
            }
        }
        treasureChest.saveData();
        player.openInventory(treasureChest.createSettingsMenu(menuHolder.currentPage()));
    }

    /**
     * Listens when a player sends a chat message and attempts to handle chat input.
     *
     * @param event AsyncPlayerChatEvent
     */
    @EventHandler
    public void onPlayerChatInput(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        String message = event.getMessage().trim();
        if (treasureItemEditors.containsKey(playerUuid)) {
            handleTreasureItemChanceEdit(player, message);
        } else {
            return;
        }
        event.setCancelled(true);
    }

    /**
     * Handles the editing of treasure item chance.
     *
     * @param player Player editing the chance of a treasure item.
     * @param input  Input from the player.
     */
    private void handleTreasureItemChanceEdit(Player player, String input) {
        UUID playerUuid = player.getUniqueId();
        TreasureItemEditor treasureItemEditor = treasureItemEditors.get(playerUuid);
        TreasureChest treasureChest = treasureItemEditor.treasureChest();
        if (InputUtil.attemptInputCancel(player, input, Message.TREASURE_ITEM_EDIT_CANCEL, treasureItemEditors,
                () -> MenuUtil.openSync(player, treasureChest.createSettingsMenu(1)))) {
            return;
        }
        Double newChance = StringUtil.parseDouble(player, input);
        if (newChance == null) return;
        if (newChance < 0 || newChance > 100) {
            Message.TREASURE_ITEM_EDIT_INVALID_CHANCE.sendError(player);
            return;
        }
        treasureItemEditor.treasureItem().setChance(newChance);
        treasureChest.saveData();
        treasureItemEditors.remove(playerUuid);
        CommonSound.SUCCESS.play(player);
        MenuUtil.openSync(player, treasureItemEditor.treasureChest().createSettingsMenu(1));
    }

    /**
     * Listens when a player attempts to break a treasure chest and handles it.
     *
     * @param event BlockBreakEvent.
     */
    @EventHandler
    public void onTreasureChestBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getBlock().getState() instanceof Chest chest)) return;
        TreasureChest treasureChest = TreasureChest.fromChest(chest);
        if (treasureChest == null) return;

        event.setCancelled(true);
        Message.TREASURE_CHEST_REMOVE_INSTRUCTIONS.sendError(event.getPlayer(), Map.of("%id%", treasureChest.getId()));
    }

    /**
     * Listens when an entity explodes and removes any exploded treasure chests.
     *
     * @param event EntityExplodeEvent.
     */
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) return;

        removeExplodedTreasureChests(event.blockList());
    }

    /**
     * Listens when a block explodes and removes any exploded treasure chests.
     *
     * @param event BlockExplodeEvent.
     */
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if (event.isCancelled()) return;

        removeExplodedTreasureChests(event.blockList());
    }

    /**
     * Removes any treasure chests found in the passed block collection.
     *
     * @param blocks Collection of blocks to check.
     */
    private void removeExplodedTreasureChests(List<Block> blocks) {
        Iterator<Block> blockIterator = blocks.iterator();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (!(block.getState() instanceof Chest chest)) continue;
            if (TreasureChest.fromChest(chest) == null) continue;

            blockIterator.remove();
        }
    }
}

record TreasureItemEditor(TreasureChest treasureChest, TreasureItem treasureItem) {

}