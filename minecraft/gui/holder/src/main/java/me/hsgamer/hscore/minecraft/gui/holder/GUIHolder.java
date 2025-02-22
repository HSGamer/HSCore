package me.hsgamer.hscore.minecraft.gui.holder;

import me.hsgamer.hscore.minecraft.gui.common.GUIElement;
import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.common.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import me.hsgamer.hscore.minecraft.gui.holder.event.CloseEvent;
import me.hsgamer.hscore.minecraft.gui.holder.event.OpenEvent;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The base holder for Minecraft GUI implementation
 *
 * @param <T> the type of the inventory context
 */
public abstract class GUIHolder<T extends InventoryContext> implements GUIElement {
  private final UUID viewerID;
  private final AtomicReference<Map<Integer, ActionItem>> itemMapRef = new AtomicReference<>(null);
  private ButtonMap buttonMap = ButtonMap.EMPTY;
  private T inventoryContext;

  /**
   * Create a new holder
   *
   * @param viewerID the unique ID of the viewer
   */
  public GUIHolder(UUID viewerID) {
    this.viewerID = viewerID;
  }

  /**
   * Create the inventory context
   *
   * @return the inventory context
   */
  protected abstract T createInventoryContext();

  /**
   * Check if the item can be set in the slot
   *
   * @param slot the slot
   *
   * @return true if the item can be set
   */
  protected boolean canSetItem(int slot) {
    return slot >= 0 && slot < getInventoryContext().getSize();
  }

  /**
   * Handle the open event. Override this method to add custom behavior.
   *
   * @param event the event
   */
  public void handleOpen(OpenEvent event) {

  }

  /**
   * Handle the close event. Override this method to add custom behavior.
   *
   * @param event the event
   */
  public void handleClose(CloseEvent event) {

  }

  /**
   * {@inheritDoc}
   * Override this method to add custom behavior.
   */
  public void handleClick(ClickEvent event) {
    getItem(event.getSlot()).ifPresent(item -> item.callAction(event));
  }

  /**
   * Update the inventory
   */
  public void update() {
    itemMapRef.accumulateAndGet(buttonMap.getItemMap(inventoryContext), (oldMap, newMap) -> {
      if (oldMap != null) {
        for (int slot : oldMap.keySet()) {
          if (newMap != null && newMap.containsKey(slot)) continue;
          if (!canSetItem(slot)) continue;
          getInventoryContext().removeItem(slot);
        }
      }

      if (newMap != null) {
        for (Map.Entry<Integer, ActionItem> entry : newMap.entrySet()) {
          int slot = entry.getKey();
          if (!canSetItem(slot)) continue;
          getInventoryContext().setItem(slot, entry.getValue().getItem());
        }
      }

      return newMap;
    });
  }

  @Override
  public void init() {
    inventoryContext = createInventoryContext();
    update();
  }

  @Override
  public void stop() {
    inventoryContext = null;
    itemMapRef.set(null);
  }

  /**
   * Get the unique ID of the viewer
   *
   * @return the unique ID of the viewer
   */
  public UUID getViewerID() {
    return viewerID;
  }

  /**
   * Get the inventory context
   *
   * @return the inventory context
   */
  public T getInventoryContext() {
    if (inventoryContext == null) {
      throw new IllegalStateException("InventoryContext is not initialized");
    }
    return inventoryContext;
  }

  /**
   * Get the item in the slot
   *
   * @param slot the slot
   *
   * @return the item
   */
  public Optional<ActionItem> getItem(int slot) {
    return Optional.ofNullable(itemMapRef.get()).map(map -> map.get(slot));
  }

  /**
   * Get the item map
   *
   * @return the item map
   */
  public Map<Integer, ActionItem> getItemMap() {
    return Optional.ofNullable(itemMapRef.get()).map(Collections::unmodifiableMap).orElseGet(Collections::emptyMap);
  }

  /**
   * Get the button map
   *
   * @return the button map
   */
  public ButtonMap getButtonMap() {
    return buttonMap;
  }

  /**
   * Set the button map
   *
   * @param buttonMap the button map
   */
  public void setButtonMap(ButtonMap buttonMap) {
    this.buttonMap = buttonMap;
  }

  /**
   * Open the inventory
   *
   * @param viewerID the unique ID of the player
   */
  public void open(UUID viewerID) {
    getInventoryContext().open(viewerID);
  }

  /**
   * Open the inventory
   */
  public void open() {
    getInventoryContext().open();
  }
}
