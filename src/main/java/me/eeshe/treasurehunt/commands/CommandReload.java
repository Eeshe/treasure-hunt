package me.eeshe.treasurehunt.commands;

import me.eeshe.treasurehunt.models.config.Message;
import me.eeshe.penpenlib.PenPenPlugin;
import me.eeshe.penpenlib.commands.PenCommand;
import me.eeshe.penpenlib.commands.PenCommandReload;

public class CommandReload extends PenCommandReload {

    public CommandReload(PenPenPlugin plugin, PenCommand parentCommand) {
        super(plugin, parentCommand);

        setPermission("treasurehunt.reload");
        setInfoMessage(Message.RELOAD_COMMAND_INFO);
        setUsageMessage(Message.RELOAD_COMMAND_USAGE);
    }
}
