package me.hsgamer.hscore.bukkit.gui.object;

import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import org.bukkit.inventory.Inventory;

/**
 * The {@link InventorySize} of {@link Inventory}
 */
public class BukkitInventorySize implements InventorySize {
  private final Inventory inventory;

  /**
   * Create a new instance
   *
   * @param inventory the inventory
   */
  public BukkitInventorySize(Inventory inventory) {
    this.inventory = inventory;
  }

  @Override
  public int getSize() {
    return inventory.getSize();
  }

  @Override
  public int getSlotPerRow() {
    switch (inventory.getType()) {
      case CHEST:
      case ENDER_CHEST:
      case SHULKER_BOX:
        return 9;
      case DISPENSER:
      case DROPPER:
      case HOPPER:
        return 3;
      default:
        return getSize();
    }
  }
}
