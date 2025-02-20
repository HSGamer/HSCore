package me.hsgamer.hscore.bukkit.gui.holder.listener;

import me.hsgamer.hscore.bukkit.gui.common.event.BukkitClickEvent;
import me.hsgamer.hscore.bukkit.gui.common.event.BukkitDragEvent;
import me.hsgamer.hscore.bukkit.gui.holder.BukkitGUIHolder;
import me.hsgamer.hscore.bukkit.gui.holder.event.BukkitCloseEvent;
import me.hsgamer.hscore.bukkit.gui.holder.event.BukkitOpenEvent;
import me.hsgamer.hscore.minecraft.gui.common.element.GUIElement;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * The base class to listen to {@link Inventory}
 */
public abstract class BukkitInventoryListener implements GUIElement, Listener {
  protected final Plugin plugin;
  private final EventPriority clickPriority;
  private final EventPriority dragPriority;
  private final EventPriority openPriority;
  private final EventPriority closePriority;

  /**
   * Create a new provider
   *
   * @param plugin        the plugin
   * @param clickPriority the priority for click event
   * @param dragPriority  the priority for drag event
   * @param openPriority  the priority for open event
   * @param closePriority the priority for close event
   */
  protected BukkitInventoryListener(Plugin plugin, EventPriority clickPriority, EventPriority dragPriority, EventPriority openPriority, EventPriority closePriority) {
    this.plugin = plugin;
    this.clickPriority = clickPriority;
    this.dragPriority = dragPriority;
    this.openPriority = openPriority;
    this.closePriority = closePriority;
  }

  /**
   * Create a new provider with the default priority
   *
   * @param plugin the plugin
   */
  protected BukkitInventoryListener(Plugin plugin) {
    this(plugin, EventPriority.LOW, EventPriority.LOW, EventPriority.NORMAL, EventPriority.NORMAL);
  }

  /**
   * Get the holder from the inventory
   *
   * @param inventory the inventory
   *
   * @return the holder or null if not found
   */
  protected abstract @Nullable BukkitGUIHolder getHolder(Inventory inventory);

  private void handleIfDisplay(InventoryEvent event, Consumer<BukkitGUIHolder> consumer) {
    Inventory inventory = event.getInventory();
    BukkitGUIHolder holder = getHolder(inventory);
    if (holder != null) {
      consumer.accept(holder);
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
    handleIfDisplay(event, gui -> {
      boolean wasCancelled = event.isCancelled();
      event.setCancelled(true);

      gui.handleClick(new BukkitClickEvent(event));

      if (!wasCancelled && !event.isCancelled()) {
        event.setCancelled(false);
      }
    });
  }

  private void onInventoryOpen(InventoryOpenEvent event) {
    handleIfDisplay(event, gui -> gui.handleOpen(new BukkitOpenEvent(event)));
  }

  private void onInventoryClose(InventoryCloseEvent event) {
    handleIfDisplay(event, gui -> gui.handleClose(new BukkitCloseEvent(event)));
  }

  private void onInventoryDrag(InventoryDragEvent event) {
    handleIfDisplay(event, gui -> gui.handleDrag(new BukkitDragEvent(event)));
  }

  @Override
  public void init() {
    registerEvent(InventoryClickEvent.class, clickPriority, this::onInventoryClick);
    registerEvent(InventoryOpenEvent.class, openPriority, this::onInventoryOpen);
    registerEvent(InventoryCloseEvent.class, closePriority, this::onInventoryClose);
    registerEvent(InventoryDragEvent.class, dragPriority, this::onInventoryDrag);
  }

  @Override
  public void stop() {
    HandlerList.unregisterAll(this);
  }
}
