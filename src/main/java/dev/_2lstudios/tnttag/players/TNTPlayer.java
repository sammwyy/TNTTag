package dev._2lstudios.tnttag.players;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.arenas.TNTArena;
import dev._2lstudios.tnttag.arenas.TNTArenaJoinResult;
import dev._2lstudios.tnttag.utils.PlaceholderUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

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
        PlaceholderUtils.format(this, message);
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

    public void playSound(Sound sound, Location location) {
        this.getBukkitPlayer().playSound(location, sound, SoundCategory.AMBIENT, 1, 1);
    }

    public void playSound(Sound sound) {
        this.playSound(sound, this.getBukkitPlayer().getLocation());
    }

    public void sendI18nActionbar(String i18nKey) {
        String message = this.formatMessage(this.getI18nMessage(i18nKey));
        Player player = this.getBukkitPlayer();
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public void setArena(TNTArena arena) {
        this.arena = arena;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;

        if (this.getBukkitPlayer().isOnline()) {
            if (spectator) {
                this.getPlugin().getInvisibilityManager().hide(this.getBukkitPlayer());
            } else {
                this.getPlugin().getInvisibilityManager().show(this.getBukkitPlayer());
            }
        }
    }

    public void teleportToLobby() {
        if (this.getBukkitPlayer().isOnline()) {
            Location lobby = this.getPlugin().getLobbyConfig().getLocation("spawn");
            if (lobby != null) {
                this.getBukkitPlayer().teleport(lobby);
            } else {
                this.sendI18nMessage("lobby.misconfigured");
            }
        }
    }

    public void toggleTNTHead(boolean toggle) {
        if (toggle) {
            ItemStack item = new ItemStack(Material.TNT);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(this.getI18nMessage("game.tnt-item"));
            item.setItemMeta(meta);
            this.getBukkitPlayer().getInventory().setHelmet(item);
        } else {
            this.getBukkitPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
        }
    }
}
