package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.ui.BaseHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.UUID;

// TODO: Add items and actions
public class GUIHolder extends BaseHolder<GUIDisplay> {

  public GUIHolder() {
    addEventConsumer(InventoryOpenEvent.class, this::onOpen);
    addEventConsumer(InventoryClickEvent.class, this::onClick);
    addEventConsumer(InventoryCloseEvent.class, this::onClose);
  }

  @Override
  protected GUIDisplay newDisplay(UUID uuid) {
    return new GUIDisplay(uuid, this);
  }

  /**
   * Handle open event
   *
   * @param e the event
   */
  public void onOpen(InventoryOpenEvent e) {
    // EMPTY
  }

  /**
   * Handle click event
   *
   * @param e the event
   */
  public void onClick(InventoryClickEvent e) {
    // EMPTY
  }

  /**
   * Handle close event
   *
   * @param e the event
   */
  public void onClose(InventoryCloseEvent e) {
    // EMPTY
  }
}
