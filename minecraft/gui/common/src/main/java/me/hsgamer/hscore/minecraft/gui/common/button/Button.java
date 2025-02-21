package me.hsgamer.hscore.minecraft.gui.common.button;

import me.hsgamer.hscore.minecraft.gui.common.GUIElement;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The button
 */
public interface Button extends GUIElement {
  /**
   * The empty button
   */
  Button EMPTY = context -> null;

  /**
   * Get the item
   *
   * @param context the inventory context
   *
   * @return the item, or null if it doesn't want to show for any reason
   */
  @Nullable ActionItem getItem(@NotNull InventoryContext context);
}
