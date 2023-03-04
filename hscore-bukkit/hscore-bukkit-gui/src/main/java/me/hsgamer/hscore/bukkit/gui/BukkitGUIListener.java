package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.bukkit.gui.event.BukkitClickEvent;
import me.hsgamer.hscore.bukkit.gui.event.BukkitCloseEvent;
import me.hsgamer.hscore.bukkit.gui.event.BukkitDragEvent;
import me.hsgamer.hscore.bukkit.gui.event.BukkitOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

/**
 * The Listener for {@link BukkitGUIHolder} and {@link BukkitGUIDisplay}.
 * Need to register this for others to work.
 */
public class BukkitGUIListener implements Listener {
  private final Plugin plugin;

  private BukkitGUIListener(Plugin plugin, EventPriority clickPriority, EventPriority dragPriority, EventPriority openPriority, EventPriority closePriority) {
    this.plugin = plugin;
    registerEvent(InventoryClickEvent.class, clickPriority, this::onInventoryClick);
    registerEvent(InventoryDragEvent.class, dragPriority, this::onInventoryDrag);
    registerEvent(InventoryOpenEvent.class, openPriority, this::onInventoryOpen);
    registerEvent(InventoryCloseEvent.class, closePriority, this::onInventoryClose);
    registerEvent(PluginDisableEvent.class, EventPriority.MONITOR, this::onPluginDisable);
  }

  /**
   * Register the listener
   *
   * @param plugin        the plugin
   * @param clickPriority the priority of the click event
   * @param dragPriority  the priority of the drag event
   * @param openPriority  the priority of the open event
   * @param closePriority the priority of the close event
   */
  public static BukkitGUIListener init(Plugin plugin, EventPriority clickPriority, EventPriority dragPriority, EventPriority openPriority, EventPriority closePriority) {
    return new BukkitGUIListener(plugin, clickPriority, dragPriority, openPriority, closePriority);
  }

  /**
   * Register the listener
   *
   * @param plugin the plugin
   */
  public static BukkitGUIListener init(Plugin plugin) {
    return init(plugin, EventPriority.LOW, EventPriority.LOW, EventPriority.NORMAL, EventPriority.NORMAL);
  }

  private static void handleIfDisplay(InventoryEvent event, Consumer<BukkitGUIDisplay> consumer) {
    InventoryHolder holder = event.getInventory().getHolder();
    if (holder instanceof BukkitGUIDisplay) {
      consumer.accept((BukkitGUIDisplay) holder);
    }
  }

  private <T extends Event> void registerEvent(Class<T> eventClass, EventPriority priority, Consumer<T> consumer) {
    Bukkit.getPluginManager().registerEvent(eventClass, this, priority, (listener, event) -> {
      if (eventClass.isInstance(event)) {
        consumer.accept(eventClass.cast(event));
      }
    }, plugin);
  }

  private void onInventoryClick(InventoryClickEvent event) {
    handleIfDisplay(event, display -> {
      boolean wasCancelled = event.isCancelled();
      event.setCancelled(true);

      display.handleEvent(new BukkitClickEvent(event));

      if (!wasCancelled && !event.isCancelled()) {
        event.setCancelled(false);
      }
    });
  }

  private void onInventoryOpen(InventoryOpenEvent event) {
    handleIfDisplay(event, display -> display.handleEvent(new BukkitOpenEvent(event)));
  }

  private void onInventoryClose(InventoryCloseEvent event) {
    handleIfDisplay(event, display -> display.handleEvent(new BukkitCloseEvent(event)));
  }

  private void onInventoryDrag(InventoryDragEvent event) {
    handleIfDisplay(event, display -> display.handleEvent(new BukkitDragEvent(event)));
  }

  private void onPluginDisable(PluginDisableEvent event) {
    if (event.getPlugin().equals(plugin)) {
      HandlerList.unregisterAll(this);
    }
  }
}
