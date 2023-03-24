package dev._2lstudios.tnttag.players;

import org.bukkit.entity.Player;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.arenas.TNTArena;
import dev._2lstudios.tnttag.arenas.TNTArenaJoinResult;

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

    @Override
    public String formatMessage(String message) {
        String output = super.formatMessage(message);

        if (this.arena != null) {
            output = output.replace("{arena_id}", this.arena.getID())
                    .replace("{arena_time}", this.arena.getTime() + "")
                    .replace("{arena_joined}", this.arena.getLastPlayerJoined().getName())
                    .replace("{arena_leave}", this.arena.getLastPlayerQuit().getName())
                    .replace("{arena_death}", this.arena.getLastPlayerDeath().getName())
                    .replace("{arena_players}", this.arena.getAlivePlayers().size() + "")
                    .replace("{arena_min}", this.arena.getSettings().minPlayers + "")
                    .replace("{arena_max}", this.arena.getSettings().maxPlayers + "");
        }

        return output;
    }

    public TNTArena getArena() {
        return this.arena;
    }

    public boolean isSpectator() {
        return this.spectator;
    }

    public TNTArenaJoinResult join(TNTArena arena) {
        TNTArenaJoinResult result = arena.join(this);
        if (result == TNTArenaJoinResult.ALREADY_STARTED) {
            this.sendI18nMessage("join.already-started");
        } else if (result == TNTArenaJoinResult.FULL) {
            this.sendI18nMessage("join.arena-is-full");
        } else if (result == TNTArenaJoinResult.PLAYER_ALREADY_ARENA) {
            this.sendI18nMessage("join.already-in-arena");
        } else if (result == TNTArenaJoinResult.PLAYER_ALREADY_ARENA) {
            this.sendI18nMessage("join.already-in-arena");
        } else if (result == TNTArenaJoinResult.SUCCESS) {
            this.sendI18nMessage("join.success");
        }
        return result;
    }

    public void kill(boolean announce) {
        if (this.arena != null) {
            this.arena.killPlayer(this, announce);
            if (announce) {
                this.sendI18nTitle("game.death.title", "game.death.subtitle");
            }
        }
    }

    public void kill() {
        this.kill(true);
    }

    public boolean leave() {
        if (this.arena == null) {
            return false;
        } else {
            this.arena.removePlayer(this);
            return true;
        }
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
