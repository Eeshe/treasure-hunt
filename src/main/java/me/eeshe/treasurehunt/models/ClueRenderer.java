package me.eeshe.treasurehunt.models;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.map.*;
import org.jetbrains.annotations.NotNull;

public class ClueRenderer extends MapRenderer {
    private final byte xOffset;
    private final byte zOffset;

    public ClueRenderer(byte xOffset, byte zOffset) {
        this.xOffset = xOffset;
        this.zOffset = zOffset;
    }

    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        MapCursorCollection mapCursorCollection = new MapCursorCollection();
        mapCursorCollection.addCursor(new MapCursor(
                xOffset,
                zOffset,
                (byte) 0,
                MapCursor.Type.RED_X,
                true,
                (Component) null
        ));
        mapCanvas.setCursors(mapCursorCollection);
    }
}
