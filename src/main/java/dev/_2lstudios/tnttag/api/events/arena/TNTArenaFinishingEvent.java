package dev._2lstudios.tnttag.api.events.arena;

import org.bukkit.event.HandlerList;

import dev._2lstudios.tnttag.api.events.TNTEvent;
import dev._2lstudios.tnttag.arenas.TNTArena;
import dev._2lstudios.tnttag.players.TNTPlayer;

public class TNTArenaFinishingEvent extends TNTEvent {
    // Event
    private TNTArena arena;

    public TNTArenaFinishingEvent(TNTArena arena) {
        this.arena = arena;
    }

    public TNTArena getArena() {
        return this.arena;
    }

    public TNTPlayer getWinner() {
        return this.arena.getWinner();
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
