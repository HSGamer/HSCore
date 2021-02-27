package me.hsgamer.hscore.bukkit.gui.mask.multi;

import me.hsgamer.hscore.bukkit.gui.button.Button;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Per-user Get Buttons interface
 */
public interface PerUserGetButtons {
  /**
   * Get the buttons for the unique id
   *
   * @param uuid the unique id
   *
   * @return the buttons
   */
  List<Button> getButtons(UUID uuid);

  /**
   * Get the user-buttons map
   *
   * @return the user-buttons map
   */
  Map<UUID, List<Button>> getUserButtons();

  /**
   * Get the first button for the unique id
   *
   * @param uuid the unique id
   *
   * @return the first button
   */
  default Button getFirstButton(UUID uuid) {
    List<Button> buttons = getButtons(uuid);
    return buttons.isEmpty() ? null : buttons.get(0);
  }
}
