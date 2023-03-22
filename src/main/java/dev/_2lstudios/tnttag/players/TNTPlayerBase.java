package dev._2lstudios.tnttag.players;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.commands.CommandExecutor;
import dev._2lstudios.tnttag.utils.PacketUtils;
import dev._2lstudios.tnttag.utils.PlayerUtils;
import dev._2lstudios.tnttag.utils.ServerUtils;

import me.clip.placeholderapi.PlaceholderAPI;

public class TNTPlayerBase extends CommandExecutor {
    private Player bukkitPlayer;

    public TNTPlayerBase(TNTTag plugin, Player bukkitPlayer) {
        super(plugin, bukkitPlayer);
        this.bukkitPlayer = bukkitPlayer;
    }

    public Player getBukkitPlayer() {
        return this.bukkitPlayer;
    }

    @Override
    public String formatMessage(String message) {
        String output = super.formatMessage(message);

        if (this.getPlugin().hasPlugin("PlaceholderAPI")) {
            output = PlaceholderAPI.setPlaceholders(this.getBukkitPlayer(), output);
        }

        return output;
    }

    @Override
    public String getLang() {
        String lang = null;

        if (ServerUtils.hasPlayerGetLocaleAPI()) {
            lang = this.getBukkitPlayer().getLocale();
        } else {
            lang = PlayerUtils.getPlayerLocaleInLegacyWay(this.bukkitPlayer);
        }

        return lang == null ? super.getLang() : lang;
    }

    public String getName() {
        return this.bukkitPlayer.getName();
    }

    public String getLowerName() {
        return this.getName().toLowerCase();
    }

    public boolean isOnline() {
        return this.bukkitPlayer != null && this.bukkitPlayer.isOnline();
    }

    public void sendRawMessage(String component, byte type) {
        if (ServerUtils.hasChatComponentAPI()) {
            this.bukkitPlayer.spigot().sendMessage(net.md_5.bungee.chat.ComponentSerializer.parse(component));
        } else {
            PacketUtils.sendJSON(this.getBukkitPlayer(), component, type);
        }
    }

    public void sendRawMessage(String component) {
        this.sendRawMessage(component, (byte) 0);
    }

    public void sendToServer(String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            this.getBukkitPlayer().sendPluginMessage(this.getPlugin(), "BungeeCord", b.toByteArray());
            b.close();
            out.close();
        } catch (Exception e) {
            this.getBukkitPlayer().sendMessage(ChatColor.RED + "Error when trying to connect to " + server);
        }
    }

}