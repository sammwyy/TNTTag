package dev._2lstudios.tnttag.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.arenas.TNTArena;
import dev._2lstudios.tnttag.players.TNTPlayer;

public class EntityDamageListener implements Listener {
    private TNTTag plugin;

    public EntityDamageListener(TNTTag plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        TNTPlayer player = this.plugin.getPlayerManager().getPlayer((Player) e.getEntity());

        if (player.getArena() != null && e.getCause() == DamageCause.FALL) {
            TNTArena arena = player.getArena();
            if (arena.isInGame()) {
                e.setDamage(0);
            } else {
                e.setCancelled(true);
            }
        }
    }
}
