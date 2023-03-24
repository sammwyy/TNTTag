package dev._2lstudios.tnttag;

import java.io.File;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import dev._2lstudios.tnttag.api.TNTTagAPI;
import dev._2lstudios.tnttag.api.events.TNTTagEvent;
import dev._2lstudios.tnttag.arenas.TNTArenaManager;
import dev._2lstudios.tnttag.commands.CommandListener;
import dev._2lstudios.tnttag.commands.impl.TNTTagAdminCommand;
import dev._2lstudios.tnttag.commands.impl.TNTTagCommand;
import dev._2lstudios.tnttag.config.ConfigManager;
import dev._2lstudios.tnttag.config.Configuration;
import dev._2lstudios.tnttag.i18n.LanguageManager;
import dev._2lstudios.tnttag.listeners.EntityDamageByEntityListener;
import dev._2lstudios.tnttag.listeners.EntityDamageListener;
import dev._2lstudios.tnttag.listeners.PlayerJoinListener;
import dev._2lstudios.tnttag.listeners.PlayerQuitListener;
import dev._2lstudios.tnttag.players.TNTPlayerManager;
import dev._2lstudios.tnttag.tasks.TNTArenaTickTask;

public class TNTTag extends JavaPlugin {
    private TNTArenaManager arenaManager;
    private ConfigManager configManager;
    private InvisibilityManager invisibilityManager;
    private LanguageManager languageManager;
    private TNTPlayerManager playerManager;

    private void addCommand(CommandListener command) {
        command.register(this, false);
    }

    private void addListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public boolean callEvent(TNTTagEvent event) {
        this.getServer().getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    @Override
    public void onEnable() {
        // Initialize API
        new TNTTagAPI(this);

        // Instantiate managers.
        this.arenaManager = new TNTArenaManager(this);
        this.configManager = new ConfigManager(this);
        this.invisibilityManager = new InvisibilityManager(this);
        this.languageManager = new LanguageManager(this);
        this.playerManager = new TNTPlayerManager(this);

        // Load data.
        this.arenaManager.safeLoadFromDirectory(this.getFile("arenas"));
        this.languageManager.loadLanguagesSafe();
        this.playerManager.addAll();

        // Register listeners.
        this.addListener(new EntityDamageByEntityListener(this));
        this.addListener(new EntityDamageListener(this));
        this.addListener(new PlayerJoinListener(this));
        this.addListener(new PlayerQuitListener(this));

        // Register commands.
        this.addCommand(new TNTTagAdminCommand());
        this.addCommand(new TNTTagCommand());

        // Register tasks.
        BukkitScheduler scheduler = this.getServer().getScheduler();
        scheduler.runTaskTimer(this, new TNTArenaTickTask(this), 0, 20L);
    }

    // Configuration getters
    public Configuration getConfig() {
        return this.configManager.getConfig("config.yml");
    }

    public Configuration getLobbyConfig() {
        return this.configManager.getConfig("lobby.yml");
    }

    // Managers getters
    public TNTArenaManager getArenaManager() {
        return this.arenaManager;
    }

    public InvisibilityManager getInvisibilityManager() {
        return this.invisibilityManager;
    }

    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public TNTPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    // Others getters
    public File getFile(String child) {
        return new File(this.getDataFolder(), child);
    }

    public File getJarFile() {
        return this.getFile();
    }

    public boolean hasPlugin(String pluginName) {
        Plugin plugin = this.getServer().getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }
}