package me.hsgamer.hscore.bukkit.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

/**
 * The Listener for {@link GUIHolder} and {@link GUIDisplay} <br> Need to register this for others to work
 */
public class GUIListener implements Listener {
  private final Plugin plugin;

  private GUIListener(Plugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Register the listener
   *
   * @param plugin the plugin
   */
  public static void init(Plugin plugin) {
    Bukkit.getPluginManager().registerEvents(new GUIListener(plugin), plugin);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onInventoryDrag(InventoryDragEvent e) {
    if (!(e.getInventory().getHolder() instanceof GUIDisplay)) {
      return;
    }
    ((GUIDisplay) e.getInventory().getHolder()).handleEvent(e);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onInventoryClick(InventoryClickEvent e) {
    if (!(e.getInventory().getHolder() instanceof GUIDisplay)) {
      return;
    }

    boolean wasCancelled = e.isCancelled();
    e.setCancelled(true);

    ((GUIDisplay) e.getInventory().getHolder()).handleEvent(e);

    if (!wasCancelled && !e.isCancelled()) {
      e.setCancelled(false);
    }
  }

  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent e) {
    if (!(e.getInventory().getHolder() instanceof GUIDisplay)) {
      return;
    }
    ((GUIDisplay) e.getInventory().getHolder()).handleEvent(e);
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent e) {
    if (!(e.getInventory().getHolder() instanceof GUIDisplay)) {
      return;
    }
    ((GUIDisplay) e.getInventory().getHolder()).handleEvent(e);
  }

  @EventHandler
  public void onPluginDisable(PluginDisableEvent event) {
    if (event.getPlugin().equals(plugin)) {
      HandlerList.unregisterAll(this);
    }
  }
}
