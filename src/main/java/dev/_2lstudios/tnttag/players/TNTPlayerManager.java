package dev._2lstudios.tnttag.players;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import dev._2lstudios.tnttag.TNTTag;

public class TNTPlayerManager {
    private TNTTag plugin;

    private Map<Player, TNTPlayer> players;

    public TNTPlayerManager(TNTTag plugin) {
        this.plugin = plugin;
        this.players = new HashMap<>();
    }

    public TNTPlayer addPlayer(Player bukkitPlayer) {
        TNTPlayer player = new TNTPlayer(this.plugin, bukkitPlayer);
        this.players.put(bukkitPlayer, player);
        return player;
    }

    public TNTPlayer removePlayer(Player bukkitPlayer) {
        return this.players.remove(bukkitPlayer);
    }

    public TNTPlayer getPlayer(Player bukkitPlayer) {
        return this.players.get(bukkitPlayer);
    }

    public TNTPlayer getPlayer(String name) {
        Player bukkitPlayer = this.plugin.getServer().getPlayerExact(name);
        if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
            return this.getPlayer(bukkitPlayer);
        } else {
            return null;
        }
    }

    public Collection<TNTPlayer> getPlayers() {
        return this.players.values();
    }

    public void clear() {
        this.players.clear();
    }

    public void addAll() {
        for (Player bukkitPlayer : this.plugin.getServer().getOnlinePlayers()) {
            this.addPlayer(bukkitPlayer).download();
        }
    }
}