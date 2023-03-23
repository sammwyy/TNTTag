package dev._2lstudios.tnttag.arenas;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.players.TNTPlayer;

public class TNTArena {
    private TNTTag plugin;
    private TNTArenaSettings settings;

    private TNTPlayer lastPlayerDeath;
    private TNTPlayer lastPlayerJoin;
    private TNTPlayer lastPlayerQuit;
    private List<TNTPlayer> players;
    private TNTArenaState state;
    private int time;
    private TNTPlayer winner;

    public TNTArena(TNTTag plugin, TNTArenaSettings settings) {
        this.plugin = plugin;
        this.settings = settings;
        this.players = new ArrayList<>();
        this.reset();
    }

    public void reset() {
        Location lobby = this.plugin.getConfig().getLocation("lobby");
        for (TNTPlayer player : this.players) {
            player.getBukkitPlayer().teleport(lobby);
            if (player.isSpectator()) {
                player.setSpectator(false);
            }
            player.setArena(null);
        }

        this.lastPlayerDeath = null;
        this.lastPlayerJoin = null;
        this.lastPlayerQuit = null;
        this.players.clear();
        this.state = TNTArenaState.WAITING;
        this.time = 1;
        this.winner = null;
    }

    public void broadcastMessage(String i18nKey) {
        for (TNTPlayer player : this.players) {
            player.sendI18nMessage(i18nKey);
        }
    }

    public void broadcastSound(Sound sound) {
        for (TNTPlayer player : this.players) {
            player.getBukkitPlayer().playSound(player.getBukkitPlayer().getLocation(), sound, 1, 1);
        }
    }

    public void broadcastTitle(String titleKey, String subtitleKey, int fadeIn, int stay, int fadeOut) {
        for (TNTPlayer player : this.players) {
            String title = player.getI18nMessage(titleKey);
            String subtitle = player.getI18nMessage(subtitleKey);
            player.getBukkitPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    public TNTPlayer getLastPlayerDeath() {
        return this.lastPlayerDeath;
    }

    public TNTPlayer getLastPlayerJoined() {
        return this.lastPlayerJoin;
    }

    public TNTPlayer getLastPlayerQuit() {
        return this.lastPlayerQuit;
    }

    public List<TNTPlayer> getPlayers() {
        return this.players;
    }

    public List<TNTPlayer> getAlivePlayers() {
        List<TNTPlayer> players = new ArrayList<>(this.getPlayers());
        players.removeIf((player) -> player.isSpectator());
        return players;
    }

    public List<TNTPlayer> getSpectators() {
        List<TNTPlayer> players = new ArrayList<>(this.getPlayers());
        players.removeIf((player) -> !player.isSpectator());
        return players;
    }

    public TNTArenaSettings getSettings() {
        return this.settings;
    }

    public TNTArenaState getState() {
        return this.state;
    }

    public TNTPlayer getWinner() {
        return this.winner;
    }

    public int getTime() {
        return this.time;
    }

    public boolean isFull() {
        return this.getAlivePlayers().size() >= this.settings.maxPlayers;
    }

    public TNTArenaJoinResult join(TNTPlayer player) {
        if (player.getArena() != null) {
            return TNTArenaJoinResult.PLAYER_ALREADY_ARENA;
        } else if (this.isFull()) {
            return TNTArenaJoinResult.FULL;
        } else if (this.state != TNTArenaState.WAITING && this.state != TNTArenaState.STARTING) {
            return TNTArenaJoinResult.ALREADY_STARTED;
        }

        player.setArena(this);
        player.getBukkitPlayer().teleport(this.settings.spawn);
        this.players.add(player);
        this.lastPlayerJoin = player;
        return TNTArenaJoinResult.SUCCESS;
    }

    public TNTArenaJoinResult joinSpectator(TNTPlayer player) {
        if (player.getArena() != null) {
            return TNTArenaJoinResult.PLAYER_ALREADY_ARENA;
        }

        player.setArena(this);
        player.setSpectator(true);
        player.getBukkitPlayer().teleport(this.settings.spectatorSpawn);
        this.players.add(player);
        return TNTArenaJoinResult.SUCCESS;
    }

    public TNTArenaQuitResult removePlayer(TNTPlayer player) {
        if (!this.equals(player.getArena())) {
            return TNTArenaQuitResult.PLAYER_NOT_IN_ARENA;
        }

        if (player.isSpectator()) {
            player.setSpectator(false);
        } else {
            this.lastPlayerQuit = player;
        }

        player.setArena(null);
        this.players.remove(player);
        return TNTArenaQuitResult.SUCCESS;
    }

    private void setState(TNTArenaState state) {
        this.state = state;
    }

    private TNTArenaState getNextState() {
        switch (this.state) {
            case WAITING:
                return TNTArenaState.STARTING;
            case STARTING:
                return TNTArenaState.IN_GAME;
            case IN_GAME:
                return TNTArenaState.FINISHING;
            case FINISHING:
            default:
                this.reset();
                return TNTArenaState.WAITING;
        }
    }

    private void handleTick() {
        switch (this.state) {
            case WAITING:
                if (this.getPlayers().size() >= this.settings.minPlayers) {
                    this.time = this.plugin.getConfig().getInt("settings.times.starting");
                    this.setState(TNTArenaState.STARTING);
                }
                break;
            case STARTING:
                if (this.getPlayers().size() > this.settings.minPlayers) {
                    this.time = 1;
                    this.setState(TNTArenaState.WAITING);
                }
                break;
            case IN_GAME:
                List<TNTPlayer> alives = this.getAlivePlayers();
                if (alives.size() == 1) {
                    this.winner = alives.get(0);
                    this.time = this.plugin.getConfig().getInt("settings.times.finishing");
                    this.setState(TNTArenaState.FINISHING);
                }
                break;
            case FINISHING:
                break;
        }
    }

    public void tick() {
        if (this.time == 0) {
            this.setState(this.getNextState());
        } else {
            this.handleTick();
            if (this.state != TNTArenaState.WAITING) {
                this.time--;
            }
        }
    }
}
