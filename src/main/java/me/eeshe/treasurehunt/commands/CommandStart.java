package me.eeshe.treasurehunt.commands;

import me.eeshe.penpenlib.PenPenPlugin;
import me.eeshe.penpenlib.commands.PenCommand;
import me.eeshe.treasurehunt.TreasureHunt;
import me.eeshe.treasurehunt.models.TreasureChest;
import me.eeshe.treasurehunt.models.config.Message;
import me.eeshe.treasurehunt.util.CompletionUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CommandStart extends PenCommand {

    public CommandStart(PenPenPlugin plugin, PenCommand parentCommand) {
        super(plugin, parentCommand);

        setName("start");
        setPermission("treasurehunt.start");
        setInfoMessage(Message.START_COMMAND_INFO);
        setUsageMessage(Message.START_COMMAND_USAGE);
        setArgumentAmount(1);
        setPlayerCommand(true);
        setCompletions(Map.of(0, (sender, strings) -> CompletionUtil.getTreasureChestIds()));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(getPlugin() instanceof TreasureHunt plugin)) return;

        Player player = (Player) sender;
        String id = args[0];
        TreasureChest treasureChest = TreasureChest.fromId(id);
        if (treasureChest == null) {
            Message.TREASURE_CHEST_NOT_FOUND.sendError(sender, Map.of("%id%", id));
            return;
        }
        plugin.getTreasureChestManager().startHunt(player, treasureChest);
    }
}
