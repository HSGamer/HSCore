package me.hsgamer.hscore.bukkit.gui.button;

import me.hsgamer.hscore.ui.property.Initializable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The button map used by {@link me.hsgamer.hscore.bukkit.gui.GUIHolder}
 */
public interface ButtonMap extends Initializable {
  /**
   * Get the button-slot map for the unique id
   *
   * @param uuid the unique id
   *
   * @return the button-slot map
   */
  Map<Button, List<Integer>> getButtons(UUID uuid);

  /**
   * Get the default button for the unique id
   *
   * @param uuid the unique id
   *
   * @return the default button
   */
  default Button getDefaultButton(UUID uuid) {
    return Button.EMPTY;
  }
}
