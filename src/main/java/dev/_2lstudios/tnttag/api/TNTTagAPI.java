package dev._2lstudios.tnttag.api;

import org.bukkit.entity.Player;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.players.TNTPlayer;

public class TNTTagAPI {
    private static TNTTag plugin;

    public TNTTagAPI(TNTTag plugin) {
        TNTTagAPI.plugin = plugin;
    }

    public static TNTPlayer getPlayer(Player player) {
        return plugin.getPlayerManager().getPlayer(player);
    }
}
