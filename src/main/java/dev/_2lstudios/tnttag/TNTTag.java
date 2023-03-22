package dev._2lstudios.tnttag;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.tnttag.api.ComplexAPI;
import dev._2lstudios.tnttag.api.events.ComplexEvent;
import dev._2lstudios.tnttag.commands.CommandListener;
import dev._2lstudios.tnttag.commands.impl.HelloCommand;
import dev._2lstudios.tnttag.config.ConfigManager;
import dev._2lstudios.tnttag.config.Configuration;
import dev._2lstudios.tnttag.i18n.LanguageManager;
import dev._2lstudios.tnttag.listeners.PlayerJoinListener;
import dev._2lstudios.tnttag.listeners.PlayerQuitListener;
import dev._2lstudios.tnttag.players.TNTPlayerManager;

public class TNTTag extends JavaPlugin {
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private TNTPlayerManager playerManager;

    private void addCommand(CommandListener command) {
        command.register(this, false);
    }

    private void addListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public boolean callEvent(ComplexEvent event) {
        this.getServer().getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    @Override
    public void onEnable() {
        // Initialize API
        new ComplexAPI(this);

        // Instantiate managers.
        this.configManager = new ConfigManager(this);
        this.languageManager = new LanguageManager(this);
        this.playerManager = new TNTPlayerManager(this);

        // Load data.
        this.languageManager.loadLanguagesSafe();
        this.playerManager.addAll();

        // Register listeners.
        this.addListener(new PlayerJoinListener(this));
        this.addListener(new PlayerQuitListener(this));

        // Register commands.
        this.addCommand(new HelloCommand());
    }

    // Configuration getters
    public Configuration getConfig() {
        return this.configManager.getConfig("config.yml");
    }

    // Managers getters
    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public TNTPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    // Others getters
    public boolean hasPlugin(String pluginName) {
        Plugin plugin = this.getServer().getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }
}