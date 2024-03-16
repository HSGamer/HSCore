package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import me.hsgamer.hscore.ui.property.Initializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface for all masks
 */
public interface Mask extends Initializable {
  /**
   * Create an empty mask
   *
   * @param name the name of the mask
   *
   * @return the mask
   */
  static Mask empty(String name) {
    return new Mask() {
      @Override
      public Optional<Map<@NotNull Integer, @NotNull Button>> generateButtons(@NotNull UUID uuid, @NotNull InventorySize inventorySize) {
        return Optional.empty();
      }

      @Override
      public @NotNull String getName() {
        return name;
      }
    };
  }

  /**
   * Generate the buttons for the unique id
   *
   * @param uuid          the unique id
   * @param inventorySize the size of the inventory
   *
   * @return the map contains the slots and the buttons
   */
  Optional<Map<@NotNull Integer, @NotNull Button>> generateButtons(@NotNull UUID uuid, @NotNull InventorySize inventorySize);

  /**
   * Get the name of the mask
   */
  @NotNull
  String getName();
}
