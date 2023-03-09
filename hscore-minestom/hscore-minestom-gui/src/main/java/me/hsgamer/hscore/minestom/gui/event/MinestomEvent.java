package me.hsgamer.hscore.minestom.gui.event;

import net.minestom.server.event.Event;

/**
 * The base event for Minestom
 *
 * @param <T> the type of the Minestom event
 */
public class MinestomEvent<T extends Event> {
  protected final T event;

  /**
   * Create a new event
   *
   * @param event the Minestom event
   */
  public MinestomEvent(T event) {
    this.event = event;
  }

  /**
   * Get the event
   *
   * @return the Minestom event
   */
  public T getEvent() {
    return event;
  }
}
