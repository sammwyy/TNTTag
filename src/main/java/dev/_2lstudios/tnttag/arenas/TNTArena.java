package dev._2lstudios.tnttag.arenas;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.players.TNTPlayer;

public class TNTArena {
    private TNTTag plugin;
    private String id;
    private TNTArenaSettings settings;

    private TNTPlayer lastPlayerDeath;
    private TNTPlayer lastPlayerJoin;
    private TNTPlayer lastPlayerQuit;
    private List<TNTPlayer> players;
    private TNTArenaState state;
    private int time;
    private TNTPlayer winner;

    public TNTArena(TNTTag plugin, String id, TNTArenaSettings settings) {
        this.plugin = plugin;
        this.id = id;
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

    public void broadcastSound(Sound sound, Location location) {
        for (TNTPlayer player : this.players) {
            player.getBukkitPlayer().playSound(location, sound, 1, 1);
        }
    }

    public void broadcastTitle(String titleKey, String subtitleKey, int fadeIn, int stay, int fadeOut) {
        for (TNTPlayer player : this.players) {
            player.sendI18nTitle(titleKey, subtitleKey, fadeIn, stay, fadeOut);
        }
    }

    public String getID() {
        return this.id;
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

    public boolean isInGame() {
        return this.state == TNTArenaState.IN_GAME || this.state == TNTArenaState.IN_GAME;
    }

    public boolean isAvailableToJoin() {
        return !this.isFull() && !this.isInGame();
    }

    public TNTArenaJoinResult join(TNTPlayer player) {
        if (player.getArena() != null) {
            return TNTArenaJoinResult.PLAYER_ALREADY_ARENA;
        } else if (this.isFull()) {
            return TNTArenaJoinResult.FULL;
        } else if (this.isInGame()) {
            return TNTArenaJoinResult.ALREADY_STARTED;
        }

        player.setArena(this);
        player.getBukkitPlayer().teleport(this.settings.spawn);
        this.players.add(player);
        this.lastPlayerJoin = player;
        this.broadcastMessage("game.join");
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

    public void killPlayer(TNTPlayer player, boolean announce) {
        player.setSpectator(true);
        if (announce) {
            this.broadcastMessage("game.death.message");
            this.broadcastSound(Sound.ENTITY_LIGHTNING_BOLT_THUNDER, player.getBukkitPlayer().getLocation());
        }
    }

    public void killPlayer(TNTPlayer player) {
        this.killPlayer(player, true);
    }

    public TNTArenaQuitResult removePlayer(TNTPlayer player) {
        if (!this.equals(player.getArena())) {
            return TNTArenaQuitResult.PLAYER_NOT_IN_ARENA;
        }

        if (player.isSpectator()) {
            player.setSpectator(false);
            player.setArena(null);
        } else {
            this.lastPlayerQuit = player;
            player.setArena(null);
            this.broadcastMessage("game.quit");
        }

        this.players.remove(player);
        player.teleportToLobby();
        return TNTArenaQuitResult.SUCCESS;
    }

    private void setState(TNTArenaState state) {
        this.state = state;

        if (state == TNTArenaState.WAITING) {
            this.broadcastMessage("game.not-enough-players");
        } else if (state == TNTArenaState.STARTING) {
            this.broadcastMessage("game.enough-players");
        }
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
                } else {
                    if (this.time >= 10 && this.time < 0) {
                        this.broadcastMessage("game.starting");
                    }
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
