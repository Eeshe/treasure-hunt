package me.eeshe.treasurehunt.commands;

import me.eeshe.penpenlib.PenPenPlugin;
import me.eeshe.penpenlib.commands.PenCommand;
import me.eeshe.treasurehunt.models.TreasureChest;
import me.eeshe.treasurehunt.models.config.Message;
import me.eeshe.treasurehunt.models.config.Sound;
import me.eeshe.treasurehunt.util.CompletionUtil;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class CommandRemoveChest extends PenCommand {

    public CommandRemoveChest(PenPenPlugin plugin, PenCommand parentCommand) {
        super(plugin, parentCommand);

        setName("removechest");
        setPermission("treasurehunt.removechest");
        setInfoMessage(Message.REMOVE_CHEST_COMMAND_INFO);
        setUsageMessage(Message.REMOVE_CHEST_COMMAND_USAGE);
        setArgumentAmount(1);
        setUniversalCommand(true);
        setCompletions(Map.of(0, (sender, strings) -> CompletionUtil.getTreasureChestIds()));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String id = args[0];
        TreasureChest treasureChest = TreasureChest.fromId(id);
        if (treasureChest == null) {
            Message.TREASURE_CHEST_NOT_FOUND.sendError(sender, Map.of("%id%", id));
            return;
        }
        treasureChest.unregister();
        Message.REMOVE_CHEST_COMMAND_SUCCESS.sendSuccess(sender, Map.of("%id%", id));
        Sound.TREASURE_CHEST_DELETE.play(treasureChest.getLocation());
    }
}
