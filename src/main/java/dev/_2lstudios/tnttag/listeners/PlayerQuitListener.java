package dev._2lstudios.tnttag.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.arenas.TNTArena;
import dev._2lstudios.tnttag.players.TNTPlayer;

public class PlayerQuitListener implements Listener {
  private TNTTag plugin;

  public PlayerQuitListener(TNTTag plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) {
    TNTPlayer player = this.plugin.getPlayerManager().removePlayer(e.getPlayer());
    TNTArena arena = player.getArena();

    if (arena != null) {
      arena.explodeBomb();
    }
  }
}
