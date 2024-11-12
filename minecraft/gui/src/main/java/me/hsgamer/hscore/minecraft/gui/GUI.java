package me.hsgamer.hscore.minecraft.gui;

import me.hsgamer.hscore.minecraft.gui.event.CloseEvent;
import me.hsgamer.hscore.minecraft.gui.event.ViewerEvent;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import me.hsgamer.hscore.ui.BaseUI;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * The UI for Minecraft
 */
public abstract class GUI extends BaseUI {
  /**
   * The {@link ViewerEvent} consumer reference
   */
  private final AtomicReference<Consumer<ViewerEvent>> viewerEventConsumerRef = new AtomicReference<>(null);

  /**
   * Set the item to the slot
   *
   * @param slot the slot
   * @param item the item
   */
  public abstract void setItem(int slot, @Nullable Item item);

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
   * Set the {@link ViewerEvent} consumer
   *
   * @param consumer the consumer
   */
  public void setViewerEventConsumer(@Nullable final Consumer<ViewerEvent> consumer) {
    viewerEventConsumerRef.set(consumer);
  }

  /**
   * Set the items
   *
   * @param items the items
   */
  public void setItems(Map<Integer, Item> items) {
    InventorySize size = getInventorySize();
    size.getSlots().forEach(slot -> setItem(slot, items.get(slot)));
  }

  /**
   * Clear all items
   */
  public void clearItems() {
    setItems(Collections.emptyMap());
  }

  @Override
  public void init() {
    addEventConsumer(ViewerEvent.class, event -> {
      Consumer<ViewerEvent> consumer = viewerEventConsumerRef.get();
      if (consumer != null) {
        consumer.accept(event);
      }
    });

    addEventConsumer(CloseEvent.class, event -> {
      if (event.isRemoveDisplay()) {
        stop();
      }
    });
  }

  @Override
  public void stop() {
    super.stop();
    clearItems();
  }
}
