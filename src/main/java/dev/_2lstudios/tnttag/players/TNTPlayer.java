package dev._2lstudios.tnttag.players;

import org.bukkit.entity.Player;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.arenas.TNTArena;

public class TNTPlayer extends TNTPlayerBase {
    private TNTArena arena;
    private boolean spectator;

    public TNTPlayer(TNTTag plugin, Player bukkitPlayer) {
        super(plugin, bukkitPlayer);

        this.arena = null;
        this.spectator = false;
    }

    public void download() {
    }

    public TNTArena getArena() {
        return this.arena;
    }

    public boolean isSpectator() {
        return this.spectator;
    }

    public void kill() {
        this.setSpectator(true);
    }

    public void setArena(TNTArena arena) {
        this.arena = arena;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;

        if (spectator) {
            for (TNTPlayer player : this.arena.getPlayers()) {
                player.getBukkitPlayer().hidePlayer(this.getPlugin(), this.getBukkitPlayer());
            }
        } else {
            for (TNTPlayer player : this.arena.getPlayers()) {
                player.getBukkitPlayer().showPlayer(this.getPlugin(), this.getBukkitPlayer());
            }
        }
    }
}
