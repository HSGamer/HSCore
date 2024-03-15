package me.hsgamer.hscore.ui;

import me.hsgamer.hscore.ui.property.Initializable;
import me.hsgamer.hscore.ui.property.Updatable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The display
 */
public interface Display extends Initializable, Updatable {
  /**
   * Get the holder for the display
   *
   * @return the holder
   */
  @NotNull
  Holder<?> getHolder();

  /**
   * Get the unique id for the display
   *
   * @return the unique id
   */
  @NotNull
  UUID getUniqueId();
}
