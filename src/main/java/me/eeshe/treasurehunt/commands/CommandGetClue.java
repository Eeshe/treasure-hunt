package me.eeshe.treasurehunt.commands;

import me.eeshe.penpenlib.PenPenPlugin;
import me.eeshe.penpenlib.commands.PenCommand;
import me.eeshe.penpenlib.util.InventoryUtil;
import me.eeshe.treasurehunt.TreasureHunt;
import me.eeshe.treasurehunt.models.TreasureChest;
import me.eeshe.treasurehunt.models.config.Message;
import me.eeshe.treasurehunt.models.config.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGetClue extends PenCommand {

    public CommandGetClue(PenPenPlugin plugin, PenCommand parentCommand) {
        super(plugin, parentCommand);

        setName("getclue");
        setPermission("treasurehunt.getclue");
        setInfoMessage(Message.GET_CLUE_COMMAND_INFO);
        setUsageMessage(Message.GET_CLUE_COMMAND_USAGE);
        setArgumentAmount(0);
        setPlayerCommand(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(getPlugin() instanceof TreasureHunt plugin)) return;

        Player player = (Player) sender;
        TreasureChest huntedTreasure = plugin.getTreasureChestManager().getCurrentlyHuntedTreasureChest();
        if (huntedTreasure == null) {
            Message.NO_TREASURE_HUNT_RUNNING.sendError(player);
            return;
        }
        InventoryUtil.giveItem(player, huntedTreasure.generateClueMap());
        Sound.GET_CLUE.play(player);
    }
}
