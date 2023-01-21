package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.ui.property.Initializable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * The interface for all masks
 */
public interface Mask extends Initializable {
  static Mask empty(String name) {
    return new Mask() {
      @Override
      public @NotNull Map<@NotNull Integer, @NotNull Button> generateButtons(@NotNull UUID uuid) {
        return Collections.emptyMap();
      }

      @Override
      public @NotNull String getName() {
        return name;
      }
    };
  }

  /**
   * Check if the target can view the mask
   *
   * @param uuid the unique id of the target
   *
   * @return true if the target can view the mask
   */
  default boolean canView(@NotNull UUID uuid) {
    return true;
  }

  /**
   * Generate the buttons for the unique id
   *
   * @param uuid the unique id
   *
   * @return the map contains the slots and the buttons
   */
  @NotNull
  Map<@NotNull Integer, @NotNull Button> generateButtons(@NotNull UUID uuid);

  /**
   * Get the name of the mask
   */
  @NotNull
  String getName();
}