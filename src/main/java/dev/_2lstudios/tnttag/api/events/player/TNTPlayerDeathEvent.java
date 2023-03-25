package dev._2lstudios.tnttag.api.events.player;

import org.bukkit.event.HandlerList;

import dev._2lstudios.tnttag.api.events.TNTCancellableEvent;
import dev._2lstudios.tnttag.players.TNTPlayer;

public class TNTPlayerDeathEvent extends TNTCancellableEvent {
    // Event
    private TNTPlayer player;

    public TNTPlayerDeathEvent(TNTPlayer player) {
        this.player = player;
    }

    public TNTPlayer getPlayer() {
        return this.player;
    }

    // Handlers
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
