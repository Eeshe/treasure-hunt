package me.eeshe.treasurehunt.files.config;

import me.eeshe.penpenlib.files.config.ConfigWrapper;
import me.eeshe.treasurehunt.models.DatabaseSettings;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class MainConfig extends ConfigWrapper {
    private static final String DATABASE_PATH = "database";
    private static final String DATABASE_HOST_PATH = DATABASE_PATH + ".host";
    private static final String DATABASE_PORT_PATH = DATABASE_PATH + ".port";
    private static final String DATABASE_DATABASE_PATH = DATABASE_PATH + ".database";
    private static final String DATABASE_USER_PATH = DATABASE_PATH + ".user";
    private static final String DATABASE_PASSWORD_PATH = DATABASE_PATH + ".password";

    private static final String TREASURE_CHEST_PATH = "treasure-chest";
    private static final String TREASURE_CHEST_OPEN_PERMISSION_PATH = TREASURE_CHEST_PATH + ".open-permission";

    public MainConfig(Plugin plugin) {
        super(plugin, null, "config.yml");
    }

    @Override
    public void writeDefaults() {
        writeDefaultDatabaseSettings();
        writeTreasureChestGeneralSettings();

        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();
    }

    /**
     * Writes the default database settings.
     */
    private void writeDefaultDatabaseSettings() {
        FileConfiguration config = getConfig();

        config.addDefault(DATABASE_HOST_PATH, "localhost");
        config.addDefault(DATABASE_PORT_PATH, "3306");
        config.addDefault(DATABASE_DATABASE_PATH, "treasurehunt");
        config.addDefault(DATABASE_USER_PATH, "root");
        config.addDefault(DATABASE_PASSWORD_PATH, "password");
    }

    /**
     * Writes the default treasure chest settings.
     */
    private void writeTreasureChestGeneralSettings() {
        getConfig().addDefault(TREASURE_CHEST_OPEN_PERMISSION_PATH, "treasurehunt.opentreasure");
    }

    /**
     * Returns the configured DatabaseSettings.
     *
     * @return Configured DatabaseSettings.
     */
    public DatabaseSettings getDatabaseSettings() {
        return new DatabaseSettings(
                getConfig().getString(DATABASE_HOST_PATH),
                getConfig().getString(DATABASE_PORT_PATH),
                getConfig().getString(DATABASE_DATABASE_PATH),
                getConfig().getString(DATABASE_USER_PATH),
                getConfig().getString(DATABASE_PASSWORD_PATH)
        );
    }

    /**
     * Returns the configured treasure chest open permission.
     *
     * @return Configured treasure chest open permission.
     */
    public String getTreasureChestOpenPermission() {
        return getConfig().getString(TREASURE_CHEST_OPEN_PERMISSION_PATH);
    }
}
