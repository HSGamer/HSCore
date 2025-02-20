package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.minecraft.gui.common.button.Button;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

/**
 * The dummy button with only the item
 */
public class DummyButton implements Button {
  private final Function<UUID, Object> itemFunction;

  /**
   * Create a new button
   *
   * @param itemFunction the item function
   */
  public DummyButton(Function<@NotNull UUID, @Nullable Object> itemFunction) {
    this.itemFunction = itemFunction;
  }

  /**
   * Create a new button
   *
   * @param item the item
   */
  public DummyButton(@Nullable Object item) {
    this(uuid -> item);
  }

  @Override
  public @NotNull ActionItem getItem(InventoryContext context) {
    return new ActionItem().setItem(itemFunction.apply(context.getViewerID()));
  }
}
