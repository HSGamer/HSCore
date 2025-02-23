package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

/**
 * The mask with a list of child masks
 */
public class ListMask extends MultiMask<Map<Integer, ActionItem>> {
  @Override
  public @Nullable Map<Integer, ActionItem> apply(@NotNull InventoryContext context) {
    for (Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> mask : elements) {
      Map<Integer, ActionItem> itemMap = mask.apply(context);
      if (itemMap != null) {
        return itemMap;
      }
    }
    return null;
  }
}
