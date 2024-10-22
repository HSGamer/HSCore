package me.hsgamer.hscore.minecraft.gui;

import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.event.CloseEvent;
import me.hsgamer.hscore.minecraft.gui.event.OpenEvent;
import me.hsgamer.hscore.minecraft.gui.event.ViewerEvent;
import me.hsgamer.hscore.minecraft.gui.object.ActionItem;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import me.hsgamer.hscore.ui.BaseUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * The UI for Minecraft
 */
public abstract class GUI extends BaseUI {
  /**
   * The buttons reference
   */
  private final AtomicReference<Map<Integer, ActionItem>> itemsRef = new AtomicReference<>(Collections.emptyMap());

  /**
   * Set the item to the slot
   *
   * @param slot the slot
   * @param item the item
   */
  protected abstract void setItem(int slot, @Nullable Item item);

  /**
   * Get the size of the inventory
   *
   * @return the size
   */
  public abstract InventorySize getInventorySize();

  /**
   * Open the inventory
   *
   * @param uuid the UUID of the player
   */
  public abstract void open(UUID uuid);

  /**
   * Handle open event
   *
   * @param event the event
   */
  protected void onOpen(@NotNull final OpenEvent event) {
    // EMPTY
  }

  /**
   * Handle click event
   *
   * @param event the event
   */
  protected void onClick(@NotNull final ClickEvent event) {
    // EMPTY
  }

  /**
   * Handle close event
   *
   * @param event the event
   */
  protected void onClose(@NotNull final CloseEvent event) {
    // EMPTY
  }

  /**
   * Modify the items
   *
   * @param operator the operator
   */
  public void modifyItems(UnaryOperator<Map<Integer, ActionItem>> operator) {
    this.itemsRef.updateAndGet(buttons -> {
      Map<Integer, ActionItem> newButtons = new HashMap<>(buttons);
      return operator.apply(newButtons);
    });
  }

  /**
   * Add an item
   *
   * @param slot the slot
   * @param item the item
   */
  public void addItem(int slot, ActionItem item) {
    modifyItems(items -> {
      items.put(slot, item);
      return items;
    });
  }

  /**
   * Remove an item
   *
   * @param slot the slot
   */
  public void removeItem(int slot) {
    modifyItems(items -> {
      items.remove(slot);
      return items;
    });
  }

  /**
   * Clear all items
   */
  public void clearItems() {
    setItems(Collections.emptyMap());
  }

  /**
   * Get the items
   *
   * @return the items
   */
  public Map<Integer, ActionItem> getItems() {
    return this.itemsRef.get();
  }

  /**
   * Set the items
   *
   * @param items the items
   */
  public void setItems(Map<Integer, ActionItem> items) {
    this.itemsRef.set(items);
  }

  /**
   * Set the items and update the GUI
   *
   * @param items the items
   */
  public void updateItems(Map<Integer, ActionItem> items) {
    setItems(items);
    update();
  }

  @Override
  public void init() {
    addEventConsumer(OpenEvent.class, this::onOpen);

    addEventConsumer(ClickEvent.class, this::onClick);
    addEventConsumer(ClickEvent.class, event -> {
      if (event.isButtonExecute()) {
        int slot = event.getSlot();
        ActionItem item = getItems().get(slot);
        if (item == null) return;
        Consumer<ViewerEvent> action = item.getAction();
        if (action == null) return;
        action.accept(event);
      }
    });

    addEventConsumer(CloseEvent.class, this::onClose);
    addEventConsumer(CloseEvent.class, event -> {
      if (event.isRemoveDisplay()) {
        stop();
      }
    });

    update();
  }

  @Override
  public void stop() {
    clearItems();
  }

  @Override
  public void update() {
    InventorySize size = getInventorySize();
    Map<Integer, ActionItem> items = getItems();
    size.getSlots().forEach(slot -> setItem(slot, items.getOrDefault(slot, ActionItem.EMPTY).getItem()));
  }
}
