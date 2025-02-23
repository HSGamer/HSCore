package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The mask that views multiple masks
 */
public class HybridMask extends MultiMask<Map<Integer, ActionItem>> {
  @Override
  public @NotNull Map<Integer, ActionItem> apply(@NotNull InventoryContext context) {
    Map<Integer, ActionItem> itemMap = new HashMap<>();
    for (Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> mask : elements) {
      Map<Integer, ActionItem> map = mask.apply(context);
      if (map != null) {
        for (Map.Entry<Integer, ActionItem> entry : map.entrySet()) {
          ActionItem current = itemMap.getOrDefault(entry.getKey(), new ActionItem());
          current.apply(entry.getValue());
          itemMap.put(entry.getKey(), current);
        }
      }
    }
    return itemMap;
  }
}
