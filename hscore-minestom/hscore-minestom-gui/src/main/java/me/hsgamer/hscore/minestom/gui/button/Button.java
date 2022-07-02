package me.hsgamer.hscore.minestom.gui.button;

import me.hsgamer.hscore.ui.property.Initializable;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;

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
   * Handle action for the unique id.
   * This action will be called before {@link #handleAction(UUID, InventoryClickEvent)} so that you can cancel the click.
   *
   * @param uuid  the unique id
   * @param event the click event
   *
   * @return true if the action can be continued to {@link #handleAction(UUID, InventoryClickEvent)}
   */
  default boolean handleAction(UUID uuid, InventoryPreClickEvent event) {
    return false;
  }

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
