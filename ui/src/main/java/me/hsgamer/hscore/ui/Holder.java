package me.hsgamer.hscore.ui;

import me.hsgamer.hscore.ui.property.Initializable;
import me.hsgamer.hscore.ui.property.Updatable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * The holder for all displays
 *
 * @param <D> the type of the display
 */
public interface Holder<D extends Display> extends Initializable, Updatable {
  /**
   * Create a display with the unique id
   *
   * @param uuid the unique id
   *
   * @return the display
   */
  @NotNull
  D createDisplay(@NotNull UUID uuid);

  /**
   * Remove a display with the unique id
   *
   * @param uuid the unique id
   */
  void removeDisplay(@NotNull UUID uuid);

  /**
   * Get the display for the unique id
   *
   * @param uuid the unique id
   *
   * @return the display
   */
  Optional<@NotNull D> getDisplay(@NotNull UUID uuid);

  /**
   * Remove all displays
   */
  void removeAllDisplay();

  /**
   * Handle the event
   *
   * @param event the event
   */
  <E> void handleEvent(@NotNull E event);

  /**
   * Update the display for the unique id
   *
   * @param uuid the unique id
   */
  default void update(UUID uuid) {
    getDisplay(uuid).ifPresent(Updatable::update);
  }
}
