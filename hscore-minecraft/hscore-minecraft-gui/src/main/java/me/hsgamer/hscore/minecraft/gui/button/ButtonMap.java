package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.ui.property.Initializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * The button map used by {@link me.hsgamer.hscore.minecraft.gui.GUIHolder}
 */
public interface ButtonMap extends Initializable {
  /**
   * Get the button map that is ready to be displayed to the unique id
   *
   * @param uuid the unique id
   * @param size the size of the inventory
   *
   * @return the button map
   */
  @NotNull
  Map<@NotNull Integer, @NotNull ViewedButton> getButtons(@NotNull final UUID uuid, int size);
}
