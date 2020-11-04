package me.hsgamer.hscore.bukkit.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * The Listener for {@link GUIHolder} and {@link GUIDisplay} <br> Need to register this for others to work
 */
public class GUIListener implements Listener {

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
