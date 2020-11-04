package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.ui.BaseHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * The UI Holder for Bukkit
 */
public class GUIHolder extends BaseHolder<GUIDisplay> {

  protected final Map<Integer, Button> buttonSlotMap = new HashMap<>();
  protected String title;
  protected InventoryType inventoryType = InventoryType.CHEST;
  protected int size = InventoryType.CHEST.getDefaultSize();

  /**
   * Create a new holder
   */
  public GUIHolder() {
    addEventConsumer(InventoryOpenEvent.class, this::onOpen);
    addEventConsumer(InventoryClickEvent.class, this::onClick);
    addEventConsumer(InventoryCloseEvent.class, this::onClose);

    addEventConsumer(InventoryClickEvent.class,
      event -> Optional.ofNullable(buttonSlotMap.get(event.getRawSlot()))
        .ifPresent(button -> button.handleAction(event.getWhoClicked().getUniqueId(), event)));
  }

  /**
   * Set the title
   *
   * @param title the title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Set the inventory type
   *
   * @param inventoryType the inventory type
   */
  public void setInventoryType(InventoryType inventoryType) {
    this.inventoryType = inventoryType;
  }

  /**
   * Set the size
   *
   * @param size the size
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * Set the button
   *
   * @param slot   the slot
   * @param button the button
   */
  public void setButton(int slot, Button button) {
    buttonSlotMap.put(slot, button);
  }

  /**
   * Remove the button
   *
   * @param slot the slot
   */
  public void removeButton(int slot) {
    buttonSlotMap.remove(slot);
  }

  /**
   * Get the button
   *
   * @param slot the slot
   *
   * @return the button
   */
  public Button getButton(int slot) {
    return buttonSlotMap.get(slot);
  }

  @Override
  protected GUIDisplay newDisplay(UUID uuid) {
    return new GUIDisplay(uuid, this);
  }

  /**
   * Handle open event
   *
   * @param event the event
   */
  protected void onOpen(InventoryOpenEvent event) {
    // EMPTY
  }

  /**
   * Handle click event
   *
   * @param event the event
   */
  protected void onClick(InventoryClickEvent event) {
    // EMPTY
  }

  /**
   * Handle close event
   *
   * @param event the event
   */
  protected void onClose(InventoryCloseEvent event) {
    // EMPTY
  }
}
