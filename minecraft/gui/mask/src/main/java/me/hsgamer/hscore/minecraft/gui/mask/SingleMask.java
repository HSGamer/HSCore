package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.GUIElement;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/**
 * The simple mask with a single button
 */
public class SingleMask implements GUIElement, Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> {
  protected final int slot;
  protected final Function<@NotNull InventoryContext, @Nullable ActionItem> button;

  /**
   * Create a new mask
   *
   * @param slot   the slot
   * @param button the button
   */
  public SingleMask(int slot, @NotNull Function<@NotNull InventoryContext, @Nullable ActionItem> button) {
    this.slot = slot;
    this.button = button;
  }

  @Override
  public void init() {
    GUIElement.handleIfElement(this.button, GUIElement::init);
  }

  @Override
  public void stop() {
    GUIElement.handleIfElement(this.button, GUIElement::stop);
  }

  @Override
  public @Nullable Map<Integer, ActionItem> apply(@NotNull InventoryContext context) {
    ActionItem actionItem = this.button.apply(context);
    return actionItem == null ? null : Collections.singletonMap(this.slot, actionItem);
  }
}
