package me.hsgamer.hscore.bukkit.gui.button;

import org.bukkit.inventory.ItemStack;

/**
 * The dummy button with only the item stack
 */
public class DummyButton extends SimpleButton {

  /**
   * Create a new button
   *
   * @param itemStack the item stack
   */
  public DummyButton(ItemStack itemStack) {
    super(itemStack, (uuid, event) -> {
    });
  }
}
