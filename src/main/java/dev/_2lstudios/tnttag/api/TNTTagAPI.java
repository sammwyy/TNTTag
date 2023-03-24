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
    public static TNTArena addArena(TNTArena arena) {
        return plugin.getArenaMAnager().addArena(arena);
    }

    public static TNTArena addArena(String id, TNTArenaSettings settings) {
        return plugin.getArenaMAnager().addArena(id, settings);
    }

    public static TNTArena getArena(String id) {
        return plugin.getArenaMAnager().getArena(id);
    }
}
