package dev._2lstudios.tnttag;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.tnttag.players.TNTPlayer;

public class InvisibilityManager implements Listener {
    private TNTTag plugin;
    public List<Player> players;

    public InvisibilityManager(TNTTag plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private boolean isPlayerHidden(Player player) {
        return this.players.contains(player);
    }

    private boolean isPlayerHidden(Entity entity) {
        if (entity instanceof Player) {
            return this.isPlayerHidden((Player) entity);
        } else {
            return false;
        }
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent e) {
        if (this.isPlayerHidden(e.getEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (this.isPlayerHidden(e.getDamager())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onEntityFoodLevelChange(FoodLevelChangeEvent e) {
        if (this.isPlayerHidden(e.getEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onEntityPickupItem(EntityPickupItemEvent e) {
        if (this.isPlayerHidden(e.getEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onEntityPotionEffect(EntityPotionEffectEvent e) {
        if (this.isPlayerHidden(e.getEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onEntityTarget(EntityTargetEvent e) {
        if (this.isPlayerHidden(e.getTarget())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onInventoryInteract(InventoryInteractEvent e) {
        if (e.getInventory() != e.getWhoClicked().getInventory()) {
            if (this.isPlayerHidden(e.getWhoClicked())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent e) {
        if (this.isPlayerHidden(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent e) {
        if (this.isPlayerHidden(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (this.isPlayerHidden(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent e) {
        if (this.isPlayerHidden(e.getPlayer())) {
            if (e.getAction() == Action.PHYSICAL) {
                e.setCancelled(true);
            } else {
                e.setUseInteractedBlock(Result.DENY);
            }
        }
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        for (Player otherPlayer : this.players) {
            player.hidePlayer(this.plugin, otherPlayer);
        }
    }

    public List<Player> getInvisiblePlayers() {
        return this.players;
    }

    public void hide(Player player) {
        this.players.add(player);

        for (Player otherPlayer : this.plugin.getServer().getOnlinePlayers()) {
            if (otherPlayer != player) {
                otherPlayer.hidePlayer(this.plugin, player);
            }
        }
    }

    public void hide(TNTPlayer player) {
        this.hide(player.getBukkitPlayer());
    }

    public void show(Player player) {
        this.players.remove(player);

        for (Player otherPlayer : this.plugin.getServer().getOnlinePlayers()) {
            if (otherPlayer != player) {
                otherPlayer.showPlayer(this.plugin, player);
            }
        }
    }

    public void show(TNTPlayer player) {
        this.show(player.getBukkitPlayer());
    }
}
