package me.hsgamer.hscore.bukkit.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

/**
 * The utility class for {@link GUIHolder}
 */
public final class GUIUtils {
  private GUIUtils() {
    // EMPTY
  }

  /**
   * Set that the holder should not cancel the click event on bottom inventory
   *
   * @param holder the gui holder
   */
  public static void allowMoveItemOnBottom(GUIHolder holder) {
    holder.addEventConsumer(InventoryClickEvent.class, event -> {
      if (event.getClickedInventory() == event.getInventory()) {
        return;
      }
      switch (event.getAction()) {
        case DROP_ALL_SLOT:
        case DROP_ONE_SLOT:
        case PICKUP_ALL:
        case PICKUP_HALF:
        case PICKUP_ONE:
        case PICKUP_SOME:
        case HOTBAR_MOVE_AND_READD:
        case PLACE_ALL:
        case PLACE_ONE:
        case PLACE_SOME:
        case HOTBAR_SWAP:
        case SWAP_WITH_CURSOR:
          event.setCancelled(false);
          break;
        default:
          break;
      }
    });
  }

  /**
   * Set that the holder cancels drag event on top inventory
   *
   * @param holder the gui holder
   */
  public static void cancelDragEvent(GUIHolder holder) {
    holder.addEventConsumer(InventoryDragEvent.class, event -> {
      for (int slot : event.getRawSlots()) {
        if (slot < event.getInventory().getSize()) {
          event.setCancelled(true);
          break;
        }
      }
    });
  }

  /**
   * Normalize the size to a valid chest size
   *
   * @param size the size
   *
   * @return the normalized size
   */
  public static int normalizeToChestSize(int size) {
    int remain = size % 9;
    size -= remain;
    size += remain > 0 ? 9 : 0;
    return size;
  }
}
