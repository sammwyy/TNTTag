package dev._2lstudios.tnttag.api.events;

import org.bukkit.event.Cancellable;

public abstract class TNTTagCancellableEvent extends TNTTagEvent implements Cancellable {
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
