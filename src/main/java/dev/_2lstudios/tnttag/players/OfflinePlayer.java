package dev._2lstudios.tnttag.players;

import org.bukkit.entity.Player;

import dev._2lstudios.tnttag.TNTTag;

public class OfflinePlayer extends TNTPlayer {
    private String username;

    public OfflinePlayer(TNTTag plugin, Player bukkitPlayer, String username) {
        super(plugin, bukkitPlayer);
        this.username = username.toLowerCase();
    }

    @Override
    public String getLowerName() {
        return this.username;
    }
}