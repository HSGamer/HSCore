package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.ui.property.Initializable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * The button map used by {@link me.hsgamer.hscore.minecraft.gui.GUIHolder}
 */
public interface ButtonMap extends Initializable {
  /**
   * Get the button-slot map for the unique id
   *
   * @param uuid the unique id
   *
   * @return the button-slot map
   */
  @NotNull
  Map<@NotNull Button, @NotNull Collection<@NotNull Integer>> getButtons(@NotNull final UUID uuid);

  /**
   * Get the default button for the unique id
   *
   * @param uuid the unique id
   *
   * @return the default button
   */
  @NotNull
  default Button getDefaultButton(@NotNull UUID uuid) {
    return Button.EMPTY;
  }
}
