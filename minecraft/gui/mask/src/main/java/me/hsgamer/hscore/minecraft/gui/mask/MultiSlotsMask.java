package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * The masks with multiple slot
 */
public class MultiSlotsMask extends MultiMask<ActionItem> {
  protected final Function<InventoryContext, List<Integer>> maskSlot;

  /**
   * Create a new mask
   *
   * @param maskSlot the mask slot
   */
  public MultiSlotsMask(@NotNull Function<InventoryContext, List<Integer>> maskSlot) {
    this.maskSlot = maskSlot;
  }

  /**
   * Get the mask slot
   *
   * @return the mask slot
   */
  @NotNull
  public Function<InventoryContext, List<Integer>> getMaskSlot() {
    return maskSlot;
  }

  @Override
  public @NotNull Map<Integer, ActionItem> apply(@NotNull InventoryContext context) {
    Map<Integer, ActionItem> map = new HashMap<>();
    List<Integer> slots = this.maskSlot.apply(context);
    if (!this.elements.isEmpty() && !slots.isEmpty()) {
      int slotsSize = slots.size();
      int buttonsSize = this.elements.size();
      for (int i = 0; i < slotsSize; i++) {
        ActionItem item = this.elements.get(i % buttonsSize).apply(context);
        if (item != null) {
          map.put(slots.get(i), item);
        }
      }
    }
    return map;
  }
}
