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
   * Handler the event
   *
   * @param eventClass the event class
   * @param event      the event
   * @param <E>        the event type
   */
  default <E> void handleEvent(@NotNull Class<E> eventClass, @NotNull E event) {
    getHolder().handleEvent(eventClass, event);
  }

  /**
   * Handle the event
   *
   * @param event the event
   */
  default void handleEvent(@NotNull Object event) {
    getHolder().handleEvent(event);
  }

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
