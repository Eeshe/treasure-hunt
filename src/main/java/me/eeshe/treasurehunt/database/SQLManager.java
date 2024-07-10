package me.eeshe.treasurehunt.database;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.eeshe.treasurehunt.TreasureHunt;
import me.eeshe.treasurehunt.models.DatabaseSettings;
import me.eeshe.treasurehunt.util.LogUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class handles the connection between the plugin and any SQL based database.
 */
public class SQLManager {
    private final TreasureHunt plugin;
    private HikariDataSource hikariDataSource;

    public SQLManager(TreasureHunt plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks if the database is currently connected.
     *
     * @return True if the database is connected.
     */
    public boolean isConnected() {
        return hikariDataSource != null && !hikariDataSource.isClosed();
    }

    /**
     * Connects the plugin to the database based on the configured parameters.
     */
    public void connect() {
        if (isConnected()) return;

        LogUtil.sendInfoLog("Connecting to SQL database...");
        DatabaseSettings databaseSettings = plugin.getMainConfig().getDatabaseSettings();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + databaseSettings.host() + ":" +
                databaseSettings.port() + "/" + databaseSettings.database());
        config.setUsername(databaseSettings.user());
        config.setPassword(databaseSettings.password());
        config.setLeakDetectionThreshold(10000);
        config.setMaximumPoolSize(10);
        config.addDataSourceProperty("useSSL", "false");
        config.addDataSourceProperty("characterEncoding", "utf8");

        try {
            this.hikariDataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnects the plugin from the database.
     */
    public void disconnect() {
        LogUtil.sendInfoLog("Disconnecting from SQL database...");
        hikariDataSource.close();
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}

