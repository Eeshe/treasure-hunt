package me.eeshe.treasurehunt;

import me.eeshe.penpenlib.PenPenLib;
import me.eeshe.penpenlib.PenPenPlugin;
import me.eeshe.penpenlib.files.config.ConfigWrapper;
import me.eeshe.penpenlib.managers.DataManager;
import me.eeshe.treasurehunt.commands.TreasureHuntCommand;
import me.eeshe.treasurehunt.database.SQLManager;
import me.eeshe.treasurehunt.files.config.MainConfig;
import me.eeshe.treasurehunt.listeners.LeaderboardMenuHandler;
import me.eeshe.treasurehunt.listeners.PlayerConnectionHandler;
import me.eeshe.treasurehunt.listeners.TreasureChestHandler;
import me.eeshe.treasurehunt.managers.TreasureChestManager;
import me.eeshe.treasurehunt.managers.TreasureHunterManager;
import me.eeshe.treasurehunt.models.config.Menu;
import me.eeshe.treasurehunt.models.config.Message;
import me.eeshe.treasurehunt.models.config.Sound;
import me.eeshe.treasurehunt.util.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class TreasureHunt extends JavaPlugin implements PenPenPlugin {
    private final List<ConfigWrapper> configFiles = new ArrayList<>();
    private final List<DataManager> dataManagers = new ArrayList<>();

    private MainConfig mainConfig;

    private SQLManager sqlManager;
    private TreasureChestManager treasureChestManager;
    private TreasureHunterManager treasureHunterManager;

    /**
     * Creates and returns a static instance of the Plugin's main class.
     *
     * @return Instance of the main class of the plugin.
     */
    public static TreasureHunt getInstance() {
        return TreasureHunt.getPlugin(TreasureHunt.class);
    }

    @Override
    public void onEnable() {
        setupFiles();
        if (!setupDatabase()) return;
        registerManagers();
        registerCommands();
        registerListeners();
        for (DataManager dataManager : dataManagers) {
            dataManager.onEnable();
        }
    }

    /**
     * Creates and configures all the config files of the plugin.
     */
    public void setupFiles() {
        configFiles.clear();

        this.mainConfig = new MainConfig(this);
        Message message = new Message();
        Sound sound = new Sound();
        Menu menu = new Menu();
        configFiles.addAll(List.of(
                mainConfig,
                message.getConfigWrapper(),
                sound.getConfigWrapper(),
                menu.getConfigWrapper()
        ));
        message.register();
        sound.register();
        menu.register();
        for (ConfigWrapper configFile : configFiles) {
            configFile.writeDefaults();
        }
    }

    /**
     * Attempts to setup the MySQL database.
     *
     * @return True if the database was successfully setup.
     */
    private boolean setupDatabase() {
        if (sqlManager == null) {
            // Only create the instance if it doesn't exist to avoid errors during plugin reload
            this.sqlManager = new SQLManager(this);
        }
        sqlManager.connect();
        if (sqlManager.isConnected()) return true;

        LogUtil.sendWarnLog("Failed to connect to SQL database.");
        LogUtil.sendWarnLog("The plugin requires a MySQL database to work, please solve the issue and restart the server.");
        getServer().getPluginManager().disablePlugin(this);
        return false;
    }

    /**
     * Registers all the needed managers in order for the plugin to work.
     */
    private void registerManagers() {
        this.treasureChestManager = new TreasureChestManager(this, sqlManager);
        this.treasureHunterManager = new TreasureHunterManager(this, sqlManager);
        dataManagers.addAll(List.of(
                treasureChestManager,
                treasureHunterManager
        ));
    }

    /**
     * Registers all the commands, subcommands, CommandExecutors and TabCompleters regarding the plugin.
     */
    private void registerCommands() {
        if (!(Bukkit.getPluginManager().getPlugin("PenPenLib") instanceof PenPenLib penPenLib)) return;

        penPenLib.registerCommands(List.of(
                new TreasureHuntCommand(this)
        ));
    }

    /**
     * Registers all the event listeners the plugin might need.
     */
    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerConnectionHandler(treasureHunterManager), this);
        pluginManager.registerEvents(new TreasureChestHandler(this), this);
        pluginManager.registerEvents(new LeaderboardMenuHandler(), this);
    }

    @Override
    public void onDisable() {
        sqlManager.disconnect();
        for (DataManager dataManager : dataManagers) {
            dataManager.unload();
        }
    }

    @Override
    public void reload() {
        sqlManager.disconnect();
        for (ConfigWrapper configFile : configFiles) {
            configFile.reloadConfig();
        }
        setupFiles();
        setupDatabase();
        for (DataManager dataManager : dataManagers) {
            dataManager.reload();
        }
    }

    @Override
    public Plugin getSpigotPlugin() {
        return this;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public TreasureChestManager getTreasureChestManager() {
        return treasureChestManager;
    }

    public TreasureHunterManager getTreasureHunterManager() {
        return treasureHunterManager;
    }
}
