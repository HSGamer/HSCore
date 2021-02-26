package me.hsgamer.hscore.bukkit.gui.advanced;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.ui.property.Initializable;
import me.hsgamer.hscore.ui.property.Updatable;

import java.util.Map;
import java.util.UUID;

public interface Mask extends Initializable, Updatable {

  /**
   * Generate the buttons for the unique id
   *
   * @param uuid the unique id
   *
   * @return the map contains the slots and the buttons
   */
  Map<Integer, Button> generateButtons(UUID uuid);
}
