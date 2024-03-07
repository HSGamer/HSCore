package me.hsgamer.hscore.bukkit.gui.button.impl;

import me.hsgamer.hscore.bukkit.gui.object.BukkitItem;
import me.hsgamer.hscore.minecraft.gui.button.impl.SimpleButton;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
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
    super(new BukkitItem(new ItemStack(Material.AIR)), consumer);
  }
}
