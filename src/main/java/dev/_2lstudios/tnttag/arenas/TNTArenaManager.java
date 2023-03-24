package dev._2lstudios.tnttag.arenas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.utils.FileUtils;

public class TNTArenaManager {
    private TNTTag plugin;
    private Map<String, TNTArena> arenas;

    public TNTArenaManager(TNTTag plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<>();
    }

    public boolean addArena(TNTArena arena) {
        String id = arena.getID();
        if (this.arenas.containsKey(id)) {
            return false;
        } else {
            this.arenas.put(id, arena);
            return true;
        }
    }

    public boolean addArena(String id, TNTArenaSettings settings) {
        if (this.arenas.containsKey(id)) {
            return false;
        } else {
            TNTArena arena = new TNTArena(this.plugin, id, settings);
            this.arenas.put(id, arena);
            return true;
        }
    }

    public boolean addArena(String id) {
        return this.addArena(id, new TNTArenaSettings());
    }

    public boolean deleteArena(String arenaID) {
        TNTArena arena = this.arenas.remove(arenaID);
        if (arena != null) {
            arena.getSettings().delete();
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteArena(TNTArena arena) {
        return this.deleteArena(arena.getID());
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

    public void loadFromFile(String id, File file) throws IOException {
        TNTArenaSettings settings = TNTArenaSettings.load(file);
        this.addArena(id, settings);
    }

    public void loadFromDirectory(File directory) throws IOException {
        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".json")) {
                String id = FileUtils.getBaseName(file);
                this.loadFromFile(id, file);
            }
        }
    }

    public boolean removeArena(String arenaID) {
        TNTArena removed = this.arenas.remove(arenaID);
        return removed != null;
    }

    public boolean removeArena(TNTArena arena) {
        return this.removeArena(arena.getID());
    }

    public void safeLoadFromDirectory(File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            this.loadFromDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tick() {
        for (TNTArena arena : this.getArenas()) {
            arena.tick();
        }
    }
}
