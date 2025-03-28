package me.hsgamer.hscore.minecraft.gui.common.inventory;

import java.util.UUID;

/**
 * The context of the inventory
 */
public interface InventoryContext {
  /**
   * Get the unique ID of the viewer
   *
   * @return the unique ID of the viewer
   */
  UUID getViewerID();

  /**
   * Get the size of the inventory
   *
   * @return the size of the inventory
   */
  int getSize();

  /**
   * Get the inventory slot from x and y coordinate
   *
   * @param x the x coordinate
   * @param y the y coordinate
   *
   * @return the slot
   */
  int getSlot(int x, int y);

  /**
   * Get the inventory slot from the position
   *
   * @param position the position
   *
   * @return the slot
   */
  default int getSlot(InventoryPosition position) {
    return getSlot(position.getX(), position.getY());
  }
}
