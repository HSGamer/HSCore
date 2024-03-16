package me.hsgamer.hscore.minecraft.gui.object;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The size of the inventory
 */
public interface InventorySize {
  /**
   * Get the size of the inventory
   *
   * @return the size
   */
  int getSize();

  /**
   * Get the slot per row
   *
   * @return the slot per row
   */
  default int getSlotPerRow() {
    return 9;
  }

  /**
   * Get the slots
   *
   * @return the slots
   */
  default IntStream getSlots() {
    return IntStream.range(0, getSize());
  }

  /**
   * Convert the slot to the position
   *
   * @param slot the slot
   *
   * @return the position
   */
  default InventoryPosition toPosition(int slot) {
    int slotPerRow = getSlotPerRow();
    int x = slot % slotPerRow;
    int y = slot / slotPerRow;
    return InventoryPosition.of(x, y);
  }

  /**
   * Convert the position to the slot
   *
   * @param positions the positions
   *
   * @return the slots
   */
  default IntStream toSlots(Stream<InventoryPosition> positions) {
    return positions.mapToInt(position -> position.toSlot(this));
  }
}
