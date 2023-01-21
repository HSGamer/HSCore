package me.hsgamer.hscore.ui;

import me.hsgamer.hscore.ui.property.Initializable;
import me.hsgamer.hscore.ui.property.Updatable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

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
   * Add an event consumer
   *
   * @param eventClass    the event class
   * @param eventConsumer the event consumer
   * @param <T>           the type of the event
   */
  <T> void addEventConsumer(@NotNull Class<T> eventClass, @NotNull Consumer<T> eventConsumer);

  /**
   * Clear all event consumers
   *
   * @param eventClass the event class
   */
  void clearEventConsumer(@NotNull Class<?> eventClass);

  /**
   * Clear all event consumers of all events
   */
  void clearAllEventConsumer();

  /**
   * Remove all displays
   */
  void removeAllDisplay();

  <E> void handleEvent(@NotNull Class<E> eventClass, @NotNull E event);

  /**
   * Handle the event
   *
   * @param event the event
   */
  default <E> void handleEvent(@NotNull E event) {
    //noinspection unchecked
    handleEvent((Class<E>) event.getClass(), event);
  }
}
