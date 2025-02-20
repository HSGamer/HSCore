package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.minecraft.gui.common.action.Action;
import me.hsgamer.hscore.minecraft.gui.common.button.Button;
import me.hsgamer.hscore.minecraft.gui.common.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A simple button
 */
public class SimpleButton implements Button {
  private final Function<UUID, Object> itemFunction;
  private final Consumer<ClickEvent> consumer;

  /**
   * Create a new simple button
   *
   * @param itemFunction the item function
   * @param consumer     the consumer
   */
  public SimpleButton(@NotNull Function<@NotNull UUID, @Nullable Object> itemFunction, @NotNull Consumer<@NotNull ClickEvent> consumer) {
    this.itemFunction = itemFunction;
    this.consumer = consumer;
  }

  /**
   * Create a new button
   *
   * @param item     the item
   * @param consumer the consumer
   */
  public SimpleButton(@Nullable Object item, @NotNull Consumer<@NotNull ClickEvent> consumer) {
    this(uuid -> item, consumer);
  }

  /**
   * Create a new button with a null item
   *
   * @param consumer the consumer
   */
  public SimpleButton(@NotNull Consumer<@NotNull ClickEvent> consumer) {
    this((Object) null, consumer);
  }

  /**
   * Create a new button
   *
   * @param itemFunction the item function
   */
  public SimpleButton(@NotNull Function<@NotNull UUID, @Nullable Object> itemFunction) {
    this(itemFunction, event -> {
    });
  }

  /**
   * Create a new button
   *
   * @param item the item
   */
  public SimpleButton(@Nullable Object item) {
    this(uuid -> item);
  }

  @Override
  public @NotNull ActionItem getItem(@NotNull InventoryContext context) {
    return new ActionItem()
      .setItem(itemFunction.apply(context.getViewerID()))
      .setAction(new Action() {
        @Override
        public void handleClick(ClickEvent event) {
          consumer.accept(event);
        }
      });
  }
}