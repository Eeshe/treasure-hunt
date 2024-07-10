package me.eeshe.treasurehunt.commands;

import me.eeshe.penpenlib.PenPenPlugin;
import me.eeshe.penpenlib.commands.PenCommand;
import me.eeshe.treasurehunt.models.config.Message;
import me.eeshe.treasurehunt.models.config.Sound;
import me.eeshe.treasurehunt.inventories.TreasureChestManagerMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager extends PenCommand {

    public CommandManager(PenPenPlugin plugin, PenCommand parentCommand) {
        super(plugin, parentCommand);

        setName("manager");
        setPermission("treasurehunt.manager");
        setInfoMessage(Message.MANAGER_COMMAND_INFO);
        setUsageMessage(Message.MANAGER_COMMAND_USAGE);
        setArgumentAmount(0);
        setPlayerCommand(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        player.openInventory(TreasureChestManagerMenu.create(1));
        Sound.TREASURE_CHEST_MANAGER_MENU_OPEN.play(player);
    }
}
