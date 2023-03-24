package dev._2lstudios.tnttag.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.annotations.Expose;

public class TNTArenaSettingsLocation {
    @Expose
    private String world;
    @Expose
    private double x;
    @Expose
    private double y;
    @Expose
    private double z;
    @Expose
    private float yaw;
    @Expose
    private float pitch;

    private Location location;

    private void build() {
        World bukkitWorld = Bukkit.getWorld(this.world);
        this.location = new Location(bukkitWorld, x, y, z, yaw, pitch);
    }

    public Location toLocation() {
        if (this.location == null) {
            this.build();
        }
        return this.location;
    }
}
