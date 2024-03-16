package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The slot for the mask.
 * Used to get the slots for the unique id.
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
    return (uuid, size) -> slots;
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
    return (uuid, size) -> slotList;
  }

  /**
   * Create a mask slot from the slot stream
   *
   * @param slotStream the slot stream
   *
   * @return the mask slot
   */
  @NotNull
  static MaskSlot of(@NotNull IntStream slotStream) {
    return of(slotStream.boxed().collect(Collectors.toList()));
  }

  /**
   * Create a mask slot from the slot function
   *
   * @param slotFunction the slot function
   *
   * @return the mask slot
   */
  @NotNull
  static MaskSlot of(@NotNull Function<InventorySize, List<Integer>> slotFunction) {
    return (uuid, size) -> slotFunction.apply(size);
  }

  /**
   * Get the slots
   *
   * @param uuid the unique id
   * @param size the size of the inventory
   *
   * @return the slots
   */
  @NotNull
  List<Integer> getSlots(UUID uuid, InventorySize size);
}
