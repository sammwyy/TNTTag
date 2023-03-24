package dev._2lstudios.tnttag.arenas;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.players.TNTPlayer;
import dev._2lstudios.tnttag.utils.RandomUtils;

public class TNTArena {
    private TNTTag plugin;
    private String id;
    private TNTArenaSettings settings;

    private TNTPlayer lastPlayerDeath;
    private TNTPlayer lastPlayerJoin;
    private TNTPlayer lastPlayerTarget;
    private TNTPlayer lastPlayerQuit;
    private List<TNTPlayer> players;
    private List<TNTPlayer> spectators;
    private TNTArenaState state;
    private int time;
    private TNTPlayer winner;

    public TNTArena(TNTTag plugin, String id, TNTArenaSettings settings) {
        this.plugin = plugin;
        this.id = id;
        this.settings = settings;

        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.reset();
    }

    public void reset() {
        this.broadcast((player) -> {
            player.teleportToLobby();
            if (player.isSpectator()) {
                player.setSpectator(false);
            }
            player.setArena(null);
        });

        this.lastPlayerDeath = null;
        this.lastPlayerJoin = null;
        this.lastPlayerQuit = null;
        this.players.clear();
        this.spectators.clear();
        this.state = TNTArenaState.WAITING;
        this.time = 1;
        this.winner = null;
    }

    public void broadcast(Consumer<? super TNTPlayer> consumer) {
        this.players.forEach(consumer);
        this.spectators.forEach(consumer);
    }

    public void broadcastActionbar(String i18nKey) {
        this.broadcast((player) -> {
            player.sendI18nActionbar(i18nKey);
        });
    }

    public void broadcastMessage(String i18nKey) {
        this.broadcast((player) -> {
            player.sendI18nMessage(i18nKey);
        });
    }

    public void broadcastSound(Sound sound) {
        this.broadcast((player) -> {
            Player bukkitPlayer = player.getBukkitPlayer();
            bukkitPlayer.playSound(bukkitPlayer.getLocation(), sound, 1, 1);
        });
    }

    public void broadcastSound(Sound sound, Location location) {
        this.broadcast((player) -> {
            Player bukkitPlayer = player.getBukkitPlayer();
            bukkitPlayer.playSound(location, sound, 1, 1);
        });
    }

    public void broadcastSound(String soundKey) {
        this.broadcastSound(this.plugin.getConfig().getSound(soundKey));
    }

    public void broadcastTitle(String titleKey, String subtitleKey, int fadeIn, int stay, int fadeOut) {
        this.broadcast((player) -> {
            player.sendI18nTitle(titleKey, subtitleKey, fadeIn, stay, fadeOut);
        });
    }

    public void broadcastTitle(String titleKey, String subtitleKey) {
        this.broadcast((player) -> {
            player.sendI18nTitle(titleKey, subtitleKey);
        });
    }

    public void explodeBomb() {
        this.killPlayer(this.lastPlayerTarget, true);
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

    public TNTPlayer getPlayerTarget() {
        return this.lastPlayerTarget;
    }

    public List<TNTPlayer> getAlivePlayers() {
        return this.players;
    }

    public List<TNTPlayer> getSpectators() {
        return this.spectators;
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
        this.spectators.add(player);
        return TNTArenaJoinResult.SUCCESS;
    }

    public void killPlayer(TNTPlayer player, boolean announce) {
        player.setSpectator(true);

        this.players.remove(player);
        this.spectators.add(player);

        if (announce) {
            Sound sound = this.plugin.getConfig().getSound("sound.game.death");
            this.broadcastSound(sound, player.getBukkitPlayer().getLocation());
            this.broadcastMessage("game.death.message");
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
            this.broadcastMessage("game.leave");
        }

        this.players.remove(player);
        player.teleportToLobby();
        return TNTArenaQuitResult.SUCCESS;
    }

    public void setTargetPlayer(TNTPlayer player) {
        TNTPlayer last = this.lastPlayerTarget;
        if (last != null && last.isOnline()) {
            last.toggleTNTHead(false);
        }

        this.lastPlayerTarget = player;
        player.toggleTNTHead(true);
        player.playSound(this.plugin.getConfig().getSound("sounds.game.target"));
    }

    public void setTargetPlayerRandom() {
        TNTPlayer player = RandomUtils.getRandomElement(this.players);
        this.setTargetPlayer(player);
        this.broadcastMessage("game.target.message");
        player.sendI18nTitle("game.target.title", "game.target.subtitle");
    }

    // Handling
    private void setState(TNTArenaState state) {
        this.state = state;

        if (state == TNTArenaState.WAITING) {
            this.time = 1;
            this.broadcastMessage("game.not-enough-players");
            this.broadcastSound("sounds.game.waiting");
        } else if (state == TNTArenaState.STARTING) {
            this.time = this.plugin.getConfig().getInt("settings.times.starting");
            this.broadcastMessage("game.enough-players");
            this.broadcastSound("sounds.game.starting");
        } else if (state == TNTArenaState.IN_GAME) {
            this.time = -1;
            this.broadcastTitle("game.started.title", "game.started.subtitle");
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

    private void handleGameTick() {
        List<TNTPlayer> alives = this.getAlivePlayers();
        TNTPlayer target = this.getPlayerTarget();

        if (this.time >= 0) {
            this.broadcastActionbar("game.actionbar");
        }

        if (this.time <= 10 && this.time > 0 && target != null) {
            target.playSound(this.plugin.getConfig().getSound("sounds.game.countdown"));
        } else if (this.time == 0) {
            this.explodeBomb();
        } else if (this.time == -5) {
            this.time = this.plugin.getConfig().getInt("settings.times.round");
            this.setTargetPlayerRandom();
        }

        if (alives.size() == 1) {
            this.winner = alives.get(0);
            this.time = this.plugin.getConfig().getInt("settings.times.finishing");
            this.setState(TNTArenaState.FINISHING);
        }
    }

    private void handleTick() {
        switch (this.state) {
            case WAITING:
                if (this.getAlivePlayers().size() >= this.settings.minPlayers) {
                    this.setState(TNTArenaState.STARTING);
                }
                break;
            case STARTING:
                if (this.getAlivePlayers().size() > this.settings.minPlayers) {
                    this.setState(TNTArenaState.WAITING);
                } else {
                    if (this.time <= 10 && this.time > 0) {
                        this.broadcastSound("sounds.game.countdown");
                        this.broadcastMessage("game.starting");
                    }
                }
                break;
            case IN_GAME:
                this.handleGameTick();
                break;
            case FINISHING:
                break;
        }
    }

    public void tick() {
        if (this.time == 0 && this.state != TNTArenaState.IN_GAME) {
            this.setState(this.getNextState());
        } else {
            this.handleTick();
            if (this.state != TNTArenaState.WAITING) {
                this.time--;
            }
        }
    }
}
