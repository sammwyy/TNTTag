package dev._2lstudios.tnttag.arenas;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class TNTArenaSettings {
    @Expose
    public TNTArenaSettingsLocation spawn;
    @Expose
    public TNTArenaSettingsLocation spectatorSpawn;
    @Expose
    public int minPlayers;
    @Expose
    public int maxPlayers;

    private File file;
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public boolean delete() {
        if (this.file.exists()) {
            return this.file.delete();
        }
        return false;
    }

    public void save() throws IOException {
        String raw = GSON.toJson(this);
        Files.writeString(this.file.toPath(), raw, StandardCharsets.UTF_8);
    }

    public static TNTArenaSettings load(File file) throws IOException {
        String raw = Files.readString(file.toPath());
        TNTArenaSettings settings = GSON.fromJson(raw, TNTArenaSettings.class);
        settings.file = file;
        return settings;
    }

    public static TNTArenaSettings create(File file) {
        TNTArenaSettings settings = new TNTArenaSettings();
        settings.file = file;
        return settings;
    }
}
