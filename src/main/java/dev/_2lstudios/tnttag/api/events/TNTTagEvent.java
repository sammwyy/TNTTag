package dev._2lstudios.tnttag.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class TNTTagEvent extends Event implements Cancellable {
    private boolean cancel;

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
