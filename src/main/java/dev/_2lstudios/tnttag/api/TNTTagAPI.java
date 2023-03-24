package dev._2lstudios.tnttag.api;

import org.bukkit.entity.Player;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.arenas.TNTArena;
import dev._2lstudios.tnttag.arenas.TNTArenaSettings;
import dev._2lstudios.tnttag.players.TNTPlayer;

public class TNTTagAPI {
    private static TNTTag plugin;

    public TNTTagAPI(TNTTag plugin) {
        TNTTagAPI.plugin = plugin;
    }

    // Player API.
    public static TNTPlayer getPlayer(Player player) {
        return plugin.getPlayerManager().getPlayer(player);
    }

    // Arena API.
    public static boolean addArena(TNTArena arena) {
        return plugin.getArenaManager().addArena(arena);
    }

    public static boolean addArena(String id, TNTArenaSettings settings) {
        return plugin.getArenaManager().addArena(id, settings);
    }

    public static TNTArena getArena(String id) {
        return plugin.getArenaManager().getArena(id);
    }
}
