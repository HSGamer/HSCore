package me.hsgamer.hscore.bukkit.gui.mask;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.ui.property.Initializable;

import java.util.Map;
import java.util.UUID;

/**
 * The interface for all masks
 */
public interface Mask extends Initializable {

  /**
   * Generate the buttons for the unique id
   *
   * @param uuid the unique id
   *
   * @return the map contains the slots and the buttons
   */
  Map<Integer, Button> generateButtons(UUID uuid);

  /**
   * Get the name of the mask
   */
  String getName();
}
