package me.eeshe.treasurehunt.commands;

import me.eeshe.penpenlib.PenPenPlugin;
import me.eeshe.penpenlib.commands.PenCommand;
import me.eeshe.penpenlib.models.Scheduler;
import me.eeshe.treasurehunt.TreasureHunt;
import me.eeshe.treasurehunt.models.TreasureHunter;
import me.eeshe.treasurehunt.models.config.Message;
import me.eeshe.treasurehunt.models.config.Sound;
import me.eeshe.treasurehunt.inventories.LeaderboardMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandLeaderboard extends PenCommand {

    public CommandLeaderboard(PenPenPlugin plugin, PenCommand parentCommand) {
        super(plugin, parentCommand);

        setName("leaderboard");
        setPermission("treasurehunt.leaderboard");
        setInfoMessage(Message.LEADERBOARD_COMMAND_INFO);
        setUsageMessage(Message.LEADERBOARD_COMMAND_USAGE);
        setArgumentAmount(0);
        setPlayerCommand(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(getPlugin() instanceof TreasureHunt plugin)) return;

        Player player = (Player) sender;
        Message.FETCHING_LEADERBOARD.send(player, Sound.LEADERBOARD_FETCH);
        plugin.getTreasureHunterManager().fetchAllAsync().whenComplete((treasureHunters, throwable) -> {
            Map<UUID, TreasureHunter> leaderboard = new LinkedHashMap<>();
            treasureHunters.stream().sorted(Comparator.comparing(TreasureHunter::getHuntedTreasuresAmount).reversed())
                    .forEachOrdered(treasureHunter -> leaderboard.put(treasureHunter.getUuid(), treasureHunter));
            Scheduler.run(plugin, player.getLocation(), () -> {
                player.openInventory(LeaderboardMenu.create(player, leaderboard));
                Sound.LEADERBOARD_MENU_OPEN.play(player);
            });
        });
    }
}
