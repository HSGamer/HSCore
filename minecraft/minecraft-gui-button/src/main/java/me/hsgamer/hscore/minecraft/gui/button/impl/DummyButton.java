package me.hsgamer.hscore.minecraft.gui.button.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.button.DisplayButton;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

/**
 * The dummy button with only the item
 */
public class DummyButton implements Button {
  private final Function<UUID, Item> itemFunction;

  /**
   * Create a new button
   *
   * @param itemFunction the item function
   */
  public DummyButton(Function<@NotNull UUID, @Nullable Item> itemFunction) {
    this.itemFunction = itemFunction;
  }

  /**
   * Create a new button
   *
   * @param itemStack the item
   */
  public DummyButton(@Nullable Item itemStack) {
    this(uuid -> itemStack);
  }

  @Override
  public @NotNull DisplayButton display(@NotNull UUID uuid) {
    return new DisplayButton(itemFunction.apply(uuid), this, null);
  }
}
