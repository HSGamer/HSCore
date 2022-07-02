package me.hsgamer.hscore.bukkit.gui.button;

import me.hsgamer.hscore.ui.property.Initializable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * An interface for all buttons
 */
public interface Button extends Initializable {

  /**
   * The empty button
   */
  Button EMPTY = uuid -> null;

  /**
   * Get the item stack for the unique id
   *
   * @param uuid the unique id
   *
   * @return the item stack
   */
  ItemStack getItemStack(UUID uuid);

  /**
   * Handle action for the unique id
   *
   * @param uuid  the unique id
   * @param event the click event
   */
  default void handleAction(UUID uuid, InventoryClickEvent event) {
    // EMPTY
  }

  /**
   * Check if the action of this button should be set even if the display item is null
   *
   * @param uuid the unique id
   *
   * @return true if it should
   */
  default boolean forceSetAction(UUID uuid) {
    return false;
  }
}
