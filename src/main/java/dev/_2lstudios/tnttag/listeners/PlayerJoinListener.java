package dev._2lstudios.tnttag.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.players.TNTPlayer;

public class PlayerJoinListener implements Listener {
  private TNTTag plugin;

  public PlayerJoinListener(TNTTag plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    TNTPlayer player = this.plugin.getPlayerManager().addPlayer(e.getPlayer());
    player.download();
  }
}
