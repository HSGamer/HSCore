package me.hsgamer.hscore.bukkit.gui.button;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * A simple button
 */
public class SimpleButton implements Button {

  private final ItemStack itemStack;
  private final BiConsumer<UUID, InventoryClickEvent> consumer;

  /**
   * Create a new button
   *
   * @param itemStack the item stack
   * @param consumer  the consumer
   */
  public SimpleButton(ItemStack itemStack, BiConsumer<UUID, InventoryClickEvent> consumer) {
    this.itemStack = itemStack;
    this.consumer = consumer;
  }

  @Override
  public ItemStack getItemStack(UUID uuid) {
    return itemStack;
  }

  @Override
  public void handleAction(UUID uuid, InventoryClickEvent event) {
    consumer.accept(uuid, event);
  }

  @Override
  public void init() {
    // EMPTY
  }

  @Override
  public void stop() {
    // EMPTY
  }
}
