package dev._2lstudios.tnttag.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.errors.BadArgumentException;
import dev._2lstudios.tnttag.errors.MaterialNotFoundException;
import dev._2lstudios.tnttag.errors.PlayerOfflineException;
import dev._2lstudios.tnttag.errors.SoundNotFoundException;
import dev._2lstudios.tnttag.errors.WorldNotFoundException;
import dev._2lstudios.tnttag.players.TNTPlayer;

public class CommandContext {
    private TNTTag plugin;
    private CommandExecutor executor;
    private CommandArguments arguments;

    public CommandContext(TNTTag plugin, CommandSender sender, Argument[] requiredArguments) {
        if (sender instanceof Player) {
            this.executor = plugin.getPlayerManager().getPlayer((Player) sender);
        } else {
            this.executor = new CommandExecutor(plugin, sender);
        }

        this.plugin = plugin;
        this.arguments = new CommandArguments(plugin, requiredArguments);
    }

    public void parseArguments(String[] args) throws BadArgumentException, PlayerOfflineException,
            WorldNotFoundException, MaterialNotFoundException, SoundNotFoundException {
        this.arguments.parse(args);
    }

    public TNTTag getPlugin() {
        return this.plugin;
    }

    public CommandExecutor getExecutor() {
        return this.executor;
    }

    public TNTPlayer getPlayer() {
        return (TNTPlayer) this.executor;
    }

    public boolean isPlayer() {
        return this.executor.isPlayer();
    }

    public CommandArguments getArguments() {
        return this.arguments;
    }
}