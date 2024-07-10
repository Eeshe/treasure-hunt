package me.eeshe.treasurehunt.commands;

import me.eeshe.penpenlib.PenPenPlugin;
import me.eeshe.penpenlib.commands.PenCommand;
import me.eeshe.treasurehunt.models.TreasureChest;
import me.eeshe.treasurehunt.models.config.Message;
import me.eeshe.treasurehunt.models.config.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CommandAddChest extends PenCommand {

    public CommandAddChest(PenPenPlugin plugin, PenCommand parentCommand) {
        super(plugin, parentCommand);

        setName("addchest");
        setPermission("treasurehunt.addchest");
        setInfoMessage(Message.ADD_CHEST_COMMAND_INFO);
        setUsageMessage(Message.ADD_CHEST_COMMAND_USAGE);
        setArgumentAmount(1);
        setPlayerCommand(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Block targetBlock = player.getTargetBlock(null, 5);
        if (!(targetBlock.getState() instanceof Chest chest)) {
            Message.NOT_LOOKING_AT_CHEST.sendError(player);
            return;
        }
        if (TreasureChest.fromChest(chest) != null) {
            Message.ALREADY_TREASURE_CHEST.sendError(player, Map.of("%id%", TreasureChest.fromChest(chest).getId()));
            return;
        }
        String id = args[0];
        if (TreasureChest.fromId(id) != null) {
            Message.ALREADY_USED_ID.sendError(player, Map.of("%id%", id));
            return;
        }
        new TreasureChest(id, chest.getLocation()).register();
        Message.ADD_CHEST_COMMAND_SUCCESS.sendSuccess(player, Map.of("%id%", id));
        Sound.TREASURE_CHEST_CREATE.play(chest.getLocation());
    }
}
