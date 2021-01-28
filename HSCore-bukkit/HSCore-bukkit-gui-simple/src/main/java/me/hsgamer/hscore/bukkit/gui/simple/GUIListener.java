package me.hsgamer.hscore.bukkit.gui.simple;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.Plugin;

/**
 * The Listener for {@link GUIHolder} and {@link GUIDisplay} <br> Need to register this for others to work
 */
public class GUIListener implements Listener {

  /**
   * Register the listener
   *
   * @param plugin the plugin
   */
  public static void init(Plugin plugin) {
    Bukkit.getPluginManager().registerEvents(new GUIListener(), plugin);
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

}
