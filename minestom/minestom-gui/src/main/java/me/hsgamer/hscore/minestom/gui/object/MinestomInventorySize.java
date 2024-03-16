package me.hsgamer.hscore.minestom.gui.object;

import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import net.minestom.server.inventory.Inventory;

/**
 * The {@link InventorySize} of {@link Inventory}
 */
public class MinestomInventorySize implements InventorySize {
  private final Inventory inventory;

  /**
   * Create a new instance
   *
   * @param inventory the inventory
   */
  public MinestomInventorySize(Inventory inventory) {
    this.inventory = inventory;
  }

  @Override
  public int getSize() {
    return inventory.getSize();
  }

  @Override
  public int getSlotPerRow() {
    switch (inventory.getInventoryType()) {
      case CHEST_1_ROW, CHEST_2_ROW, CHEST_3_ROW, CHEST_4_ROW, CHEST_5_ROW, CHEST_6_ROW, SHULKER_BOX -> {
        return 9;
      }
      case WINDOW_3X3, CRAFTER_3X3 -> {
        return 3;
      }
      default -> {
        return getSize();
      }
    }
  }
}
