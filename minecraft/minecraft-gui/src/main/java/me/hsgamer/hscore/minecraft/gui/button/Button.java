package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.ui.property.Initializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * An interface for all buttons
 */
public interface Button extends Initializable {
  /**
   * The empty button
   */
  Button EMPTY = uuid -> null;

  /**
   * Get the display button
   *
   * @param uuid the unique id
   *
   * @return the display button
   */
  @Nullable
  DisplayButton view(@NotNull final UUID uuid);
}
