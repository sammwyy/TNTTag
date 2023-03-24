package dev._2lstudios.tnttag.arenas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dev._2lstudios.tnttag.TNTTag;

public class TNTArenaManager {
    private TNTTag plugin;
    private Map<String, TNTArena> arenas;

    public TNTArenaManager(TNTTag plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<>();
    }

    public TNTArena addArena(TNTArena arena) {
        this.arenas.put(arena.getID(), arena);
        return arena;
    }

    public TNTArena addArena(String id, TNTArenaSettings settings) {
        TNTArena arena = new TNTArena(this.plugin, id, settings);
        return this.addArena(arena);
    }

    public TNTArena getArena(String id) {
        return this.arenas.get(id);
    }

    public Collection<TNTArena> getArenas() {
        return this.arenas.values();
    }

    public List<TNTArena> getArenas(TNTArenaState state) {
        List<TNTArena> arenas = new ArrayList<>(this.getArenas());
        arenas.removeIf((arena) -> arena.getState() != state);
        return arenas;
    }

    public TNTArena cherryPicking() {
        Iterator<TNTArena> arenas = this.getArenas().iterator();
        TNTArena candidate = null;

        while (arenas.hasNext()) {
            TNTArena arena = arenas.next();

            if (arena.isAvailableToJoin()) {
                if (candidate == null) {
                    candidate = arena;
                } else {
                    int candidatePlayers = candidate.getAlivePlayers().size();
                    int arenaPlayers = arena.getAlivePlayers().size();

                    if (candidatePlayers < arenaPlayers) {
                        candidate = arena;
                    }
                }
            }
        }

        return candidate;
    }
}
