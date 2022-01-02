package me.hsgamer.hscore.bukkit.gui.button;

import me.hsgamer.hscore.ui.property.Initializable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ButtonMap extends Initializable {
  Map<Button, List<Integer>> getButtons(UUID uuid);

  default Button getDefaultButton(UUID uuid) {
    return Button.EMPTY;
  }
}
