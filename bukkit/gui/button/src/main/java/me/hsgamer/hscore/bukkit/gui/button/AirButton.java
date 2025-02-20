package me.hsgamer.hscore.bukkit.gui.button;

import me.hsgamer.hscore.minecraft.gui.button.SimpleButton;
import me.hsgamer.hscore.minecraft.gui.common.event.ClickEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * The air button
 */
public class AirButton extends SimpleButton {
  /**
   * Create a new button
   *
   * @param consumer the consumer
   */
  public AirButton(Consumer<ClickEvent> consumer) {
    super(new ItemStack(Material.AIR), consumer);
  }
}
