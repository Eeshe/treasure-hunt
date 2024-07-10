package me.eeshe.treasurehunt.commands;

import me.eeshe.penpenlib.PenPenPlugin;
import me.eeshe.penpenlib.commands.PenCommand;

import java.util.List;

public class TreasureHuntCommand extends PenCommand {

    public TreasureHuntCommand(PenPenPlugin plugin) {
        super(plugin);

        setName("treasurehunt");
        setPermission("treasurehunt.base");
        setSubcommands(List.of(
                new CommandAddChest(plugin, this),
                new CommandRemoveChest(plugin, this),
                new CommandManager(plugin, this),
                new CommandStart(plugin, this),
                new CommandStop(plugin, this),
                new CommandGetClue(plugin, this),
                new CommandLeaderboard(plugin, this),
                new CommandReload(plugin, this),
                new CommandHelp(plugin, this)
        ));
    }
}
