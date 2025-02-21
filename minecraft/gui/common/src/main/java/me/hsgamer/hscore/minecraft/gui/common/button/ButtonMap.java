package me.hsgamer.hscore.minecraft.gui.common.button;

import me.hsgamer.hscore.minecraft.gui.common.GUIElement;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * The map of buttons
 */
public interface ButtonMap extends GUIElement {
  /**
   * The empty button map
   */
  ButtonMap EMPTY = context -> null;

  /**
   * Get the item map
   *
   * @param context the inventory context
   *
   * @return the item map, or null if it doesn't want to show for any reason
   */
  @Nullable Map<Integer, ActionItem> getItemMap(@NotNull InventoryContext context);
}
