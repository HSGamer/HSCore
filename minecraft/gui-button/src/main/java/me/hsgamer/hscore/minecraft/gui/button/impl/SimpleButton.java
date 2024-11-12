package me.hsgamer.hscore.minecraft.gui.button.impl;

import me.hsgamer.hscore.minecraft.gui.button.ActionItem;
import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A simple button
 */
public class SimpleButton implements Button {
  private final Function<UUID, Item> itemFunction;
  private final Consumer<ClickEvent> consumer;

  /**
   * Create a new simple button
   *
   * @param itemFunction the item function
   * @param consumer     the consumer
   */
  public SimpleButton(@NotNull Function<@NotNull UUID, @Nullable Item> itemFunction, @NotNull Consumer<@NotNull ClickEvent> consumer) {
    this.itemFunction = itemFunction;
    this.consumer = consumer;
  }

  /**
   * Create a new button
   *
   * @param item     the item
   * @param consumer the consumer
   */
  public SimpleButton(@Nullable Item item, @NotNull Consumer<@NotNull ClickEvent> consumer) {
    this(uuid -> item, consumer);
  }

  @Override
  public ActionItem display(@NotNull UUID uuid) {
    return new ActionItem()
      .setItem(itemFunction.apply(uuid))
      .setClickAction(consumer);
  }
}
