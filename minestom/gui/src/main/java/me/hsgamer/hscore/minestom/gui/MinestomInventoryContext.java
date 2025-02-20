package me.hsgamer.hscore.minestom.gui;

import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import net.minestom.server.inventory.Inventory;

import java.util.UUID;

/**
 * The {@link InventoryContext} of {@link Inventory}
 */
public class MinestomInventoryContext implements InventoryContext {
  private final UUID viewerId;
  private final Inventory inventory;

  /**
   * Create a new instance
   *
   * @param viewerId  the viewer id
   * @param inventory the inventory
   */
  public MinestomInventoryContext(UUID viewerId, Inventory inventory) {
    this.viewerId = viewerId;
    this.inventory = inventory;
  }

  public Inventory getInventory() {
    return inventory;
  }

  @Override
  public UUID getViewerID() {
    return viewerId;
  }

  @Override
  public int getSize() {
    return inventory.getSize();
  }

  @Override
  public int getSlot(int x, int y) {
    int slotPerRow = switch (inventory.getInventoryType()) {
      case CHEST_1_ROW, CHEST_2_ROW, CHEST_3_ROW, CHEST_4_ROW, CHEST_5_ROW, CHEST_6_ROW, SHULKER_BOX -> 9;
      case WINDOW_3X3, CRAFTER_3X3 -> 3;
      default -> inventory.getSize();
    };
    return x + y * slotPerRow;
  }
}
