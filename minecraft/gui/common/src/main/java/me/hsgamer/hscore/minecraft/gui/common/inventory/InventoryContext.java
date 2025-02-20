package me.hsgamer.hscore.minecraft.gui.common.inventory;

import java.util.UUID;

public interface InventoryContext {
  UUID getViewerID();

  int getInventorySize();

  int getSlot(int x, int y);

  default int getSlot(InventoryPosition position) {
    return getSlot(position.getX(), position.getY());
  }
}
