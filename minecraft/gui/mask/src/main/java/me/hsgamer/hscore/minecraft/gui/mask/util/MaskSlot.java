package me.hsgamer.hscore.minecraft.gui.mask.util;

import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * The slot for the mask
 */
public interface MaskSlot extends Function<InventoryContext, @NotNull List<Integer>> {
  /**
   * Create a mask slot from the slots
   *
   * @param slots the slots
   *
   * @return the mask slot
   */
  @NotNull
  static MaskSlot of(@NotNull List<@NotNull Integer> slots) {
    return (context) -> slots;
  }

  /**
   * Create a mask slot from the slots
   *
   * @param slots the slots
   *
   * @return the mask slot
   */
  @NotNull
  static MaskSlot of(@NotNull Integer... slots) {
    List<Integer> slotList = Arrays.asList(slots);
    return (context) -> slotList;
  }

  /**
   * Get the slots
   *
   * @return the slots
   */
  @Override
  @NotNull List<Integer> apply(InventoryContext context);
}
