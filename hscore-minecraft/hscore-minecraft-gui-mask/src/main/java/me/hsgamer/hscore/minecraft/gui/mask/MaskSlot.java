package me.hsgamer.hscore.minecraft.gui.mask;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The slot for the mask
 */
public interface MaskSlot {
  /**
   * Create a mask slot from the slots
   *
   * @param slots the slots
   *
   * @return the mask slot
   */
  @NotNull
  static MaskSlot of(@NotNull List<@NotNull Integer> slots) {
    return () -> slots;
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
    return () -> slotList;
  }

  /**
   * Create a mask slot from the slot stream
   *
   * @param slotStream the slot stream
   *
   * @return the mask slot
   */
  @NotNull
  static MaskSlot of(IntStream slotStream) {
    return of(slotStream.boxed().collect(Collectors.toList()));
  }

  /**
   * Get the slots
   *
   * @return the slots
   */
  @NotNull
  List<Integer> getSlots();
}
