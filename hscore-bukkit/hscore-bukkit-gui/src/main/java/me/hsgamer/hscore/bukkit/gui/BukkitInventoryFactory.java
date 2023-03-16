package me.hsgamer.hscore.bukkit.gui;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

/**
 * A factory to create {@link Inventory} from the {@link BukkitGUIDisplay}
 */
public interface BukkitInventoryFactory {
  /**
   * Create a new inventory
   *
   * @param display the display
   * @param uuid    the unique id
   * @param size    the size of the inventory
   * @param title   the title of the inventory
   *
   * @return the inventory
   */
  Inventory createInventory(BukkitGUIDisplay display, UUID uuid, int size, String title);
}
