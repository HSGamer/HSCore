package me.hsgamer.hscore.bukkit.gui.button.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A simple button
 */
public class SimpleButton implements Button {
  private final Function<UUID, ItemStack> itemStackFunction;
  private final BiConsumer<UUID, InventoryClickEvent> consumer;

  /**
   * Create a new simple button
   *
   * @param itemStackFunction the item stack function
   * @param consumer          the consumer
   */
  public SimpleButton(Function<UUID, ItemStack> itemStackFunction, BiConsumer<UUID, InventoryClickEvent> consumer) {
    this.itemStackFunction = itemStackFunction;
    this.consumer = consumer;
  }

  /**
   * Create a new button
   *
   * @param itemStack the item stack
   * @param consumer  the consumer
   */
  public SimpleButton(ItemStack itemStack, BiConsumer<UUID, InventoryClickEvent> consumer) {
    this(uuid -> itemStack, consumer);
  }

  @Override
  public ItemStack getItemStack(UUID uuid) {
    return itemStackFunction.apply(uuid);
  }

  @Override
  public void handleAction(UUID uuid, InventoryClickEvent event) {
    consumer.accept(uuid, event);
  }
}
