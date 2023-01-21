package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.bukkit.gui.event.BukkitClickEvent;
import me.hsgamer.hscore.bukkit.gui.event.BukkitCloseEvent;
import me.hsgamer.hscore.bukkit.gui.event.BukkitOpenEvent;
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
 * The Listener for {@link BukkitGUIHolder} and {@link BukkitGUIDisplay}.
 * Need to register this for others to work.
 */
public class BukkitGUIListener implements Listener {
  private final Plugin plugin;

  private BukkitGUIListener(Plugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Register the listener
   *
   * @param plugin the plugin
   */
  public static void init(Plugin plugin) {
    Bukkit.getPluginManager().registerEvents(new BukkitGUIListener(plugin), plugin);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onInventoryDrag(InventoryDragEvent event) {
    if (!(event.getInventory().getHolder() instanceof BukkitGUIDisplay)) {
      return;
    }
    ((BukkitGUIDisplay) event.getInventory().getHolder()).handleEvent(event);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onInventoryClick(InventoryClickEvent event) {
    if (!(event.getInventory().getHolder() instanceof BukkitGUIDisplay)) {
      return;
    }

    boolean wasCancelled = event.isCancelled();
    event.setCancelled(true);

    ((BukkitGUIDisplay) event.getInventory().getHolder()).handleEvent(new BukkitClickEvent(event));

    if (!wasCancelled && !event.isCancelled()) {
      event.setCancelled(false);
    }
  }

  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent event) {
    if (!(event.getInventory().getHolder() instanceof BukkitGUIDisplay)) {
      return;
    }
    ((BukkitGUIDisplay) event.getInventory().getHolder()).handleEvent(new BukkitOpenEvent(event));
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event) {
    if (!(event.getInventory().getHolder() instanceof BukkitGUIDisplay)) {
      return;
    }
    ((BukkitGUIDisplay) event.getInventory().getHolder()).handleEvent(new BukkitCloseEvent(event));
  }

  @EventHandler
  public void onPluginDisable(PluginDisableEvent event) {
    if (event.getPlugin().equals(plugin)) {
      HandlerList.unregisterAll(this);
    }
  }
}
