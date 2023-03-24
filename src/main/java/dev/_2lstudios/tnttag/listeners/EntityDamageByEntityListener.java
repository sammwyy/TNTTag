package dev._2lstudios.tnttag.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.arenas.TNTArena;
import dev._2lstudios.tnttag.players.TNTPlayer;

public class EntityDamageByEntityListener implements Listener {
    private TNTTag plugin;

    public EntityDamageByEntityListener(TNTTag plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) {
            return;
        }

        TNTPlayer player = this.plugin.getPlayerManager().getPlayer((Player) e.getEntity());
        TNTPlayer damager = this.plugin.getPlayerManager().getPlayer((Player) e.getDamager());

        if (player.getArena() == null || damager.getArena() == null) {
            return;
        } else if (player.getArena() != damager.getArena()) {
            return;
        }

        TNTArena arena = player.getArena();
        if (arena.getPlayerTarget() == damager) {
            arena.setTargetPlayer(player);
        } else {
            e.setCancelled(true);
        }
    }
}
