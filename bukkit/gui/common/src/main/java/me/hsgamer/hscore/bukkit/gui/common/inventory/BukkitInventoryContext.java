package me.hsgamer.hscore.bukkit.gui.common.inventory;

import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

/**
 * The {@link InventoryContext} of {@link Inventory}
 */
public class BukkitInventoryContext implements InventoryContext {
  private final UUID uuid;
  private final Inventory inventory;

  /**
   * Create a new instance
   *
   * @param uuid      the unique id of the viewer
   * @param inventory the inventory
   */
  public BukkitInventoryContext(UUID uuid, Inventory inventory) {
    this.uuid = uuid;
    this.inventory = inventory;
  }

  /**
   * Get the inventory
   *
   * @return the inventory
   */
  public Inventory getInventory() {
    return inventory;
  }

  @Override
  public UUID getViewerID() {
    return uuid;
  }

  @Override
  public int getSize() {
    return inventory.getSize();
  }

  @Override
  public int getSlot(int x, int y) {
    int slotPerRow;
    switch (inventory.getType()) {
      case CHEST:
      case ENDER_CHEST:
      case SHULKER_BOX:
        slotPerRow = 9;
        break;
      case DISPENSER:
      case DROPPER:
      case HOPPER:
        slotPerRow = 3;
        break;
      default:
        slotPerRow = getSize();
        break;
    }
    return x + y * slotPerRow;
  }
}
