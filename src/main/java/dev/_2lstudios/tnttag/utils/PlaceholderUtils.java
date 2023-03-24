package dev._2lstudios.tnttag.utils;

import org.apache.commons.lang.StringUtils;

import dev._2lstudios.tnttag.arenas.TNTArena;
import dev._2lstudios.tnttag.players.TNTPlayer;

public class PlaceholderUtils {
    public static final String NA = "N/A";

    public static String resolvePlaceholder(TNTPlayer player, String placeholder) {
        TNTArena arena = player.getArena();

        if (placeholder.equals("arena_id")) {
            return arena == null ? NA : arena.getID();
        }

        else if (placeholder.equals("arena_time")) {
            return arena == null ? NA : arena.getTime() + "";
        }

        else if (placeholder.equals("arena_joined")) {
            TNTPlayer target = arena == null ? null : arena.getLastPlayerJoined();
            return target == null ? NA : target.getName();
        }

        else if (placeholder.equals("arena_leave")) {
            TNTPlayer target = arena == null ? null : arena.getLastPlayerQuit();
            return target == null ? NA : target.getName();
        }

        else if (placeholder.equals("arena_death")) {
            TNTPlayer target = arena == null ? null : arena.getLastPlayerDeath();
            return target == null ? NA : target.getName();
        }

        else if (placeholder.equals("arena_target")) {
            TNTPlayer target = arena == null ? null : arena.getPlayerTarget();
            return target == null ? NA : target.getName();
        }

        else if (placeholder.equals("arena_players")) {
            return arena == null ? NA : arena.getAlivePlayers().size() + "";
        }

        else if (placeholder.equals("arena_spectators")) {
            return arena == null ? NA : arena.getSpectators().size() + "";
        }

        else if (placeholder.equals("arena_min")) {
            return arena == null ? NA : arena.getSettings().minPlayers + "";
        }

        else if (placeholder.equals("arena_max")) {
            return arena == null ? NA : arena.getSettings().maxPlayers + "";
        }

        else if (placeholder.equals("arena_winner")) {
            TNTPlayer target = arena == null ? null : arena.getWinner();
            return target == null ? NA : target.getName();
        }

        return "<unknown=" + placeholder + ">";
    }

    public static String format(TNTPlayer player, String message) {
        String key = null;

        do {
            key = StringUtils.substringBetween(message, "{", "}");
            if (key != null) {
                String value = PlaceholderUtils.resolvePlaceholder(player, key);
                if (value != null) {
                    message = message.replace("{" + key + "}", value);
                }
            }
        } while (key != null);

        return message;
    }
}
