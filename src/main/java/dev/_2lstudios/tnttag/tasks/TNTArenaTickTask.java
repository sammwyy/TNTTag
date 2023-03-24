package dev._2lstudios.tnttag.tasks;

import dev._2lstudios.tnttag.TNTTag;

public class TNTArenaTickTask implements Runnable {
    private TNTTag plugin;

    public TNTArenaTickTask(TNTTag plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.getArenaManager().tick();
    }
}
