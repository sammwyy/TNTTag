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
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

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

    public void sendI18nActionbar(String i18nKey) {
        String message = this.formatMessage(this.getI18nMessage(i18nKey));
        Player player = this.getBukkitPlayer();
        if (!message.isEmpty()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        }
    }

    public void sendI18nTitle(String titleKey, String subititleKey, int fadeIn, int stay, int fadeOut) {
        String title = this.formatMessage(this.getI18nMessage(titleKey));
        String subtitle = this.formatMessage(this.getI18nMessage(subititleKey));
        this.bukkitPlayer.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    public void sendI18nTitle(String titleKey, String subititleKey) {
        this.sendI18nTitle(titleKey, subititleKey, 10, 70, 20);
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