package me.eeshe.treasurehunt.util;

import me.eeshe.treasurehunt.TreasureHunt;

import java.util.ArrayList;
import java.util.List;

public class CompletionUtil {

    /**
     * Returns a list with the IDs of all configured treasure chests.
     *
     * @return A list with the IDs of all configured treasure chests.
     */
    public static List<String> getTreasureChestIds() {
        return new ArrayList<>(TreasureHunt.getInstance().getTreasureChestManager().getTreasureChests().keySet());
    }
}
