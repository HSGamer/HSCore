package me.hsgamer.hscore.bukkit.listener;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

/**
 * A wrapper for {@link org.bukkit.plugin.EventExecutor}
 *
 * @param <E> the type of the event
 */
public class ListenerExecutor<E extends Event> {
  /**
   * The type of the event
   */
  public final Class<E> eventClass;
  /**
   * The priority of the event
   */
  public final EventPriority priority;
  /**
   * whether to ignore cancelled event
   */
  public final boolean ignoreCancelled;
  /**
   * The consumer to handle the event
   */
  public final Consumer<E> eventConsumer;

  /**
   * Create a new listener executor
   *
   * @param eventClass      the class of the event
   * @param priority        the priority of the event
   * @param ignoreCancelled whether to ignore cancelled event
   * @param eventConsumer   the consumer to handle the event
   */
  public ListenerExecutor(Class<E> eventClass, EventPriority priority, boolean ignoreCancelled, Consumer<E> eventConsumer) {
    this.eventClass = eventClass;
    this.priority = priority;
    this.ignoreCancelled = ignoreCancelled;
    this.eventConsumer = eventConsumer;
  }

  /**
   * Create a new listener executor
   *
   * @param eventClass    the class of the event
   * @param priority      the priority of the event
   * @param eventConsumer the consumer to handle the event
   */
  public ListenerExecutor(Class<E> eventClass, EventPriority priority, Consumer<E> eventConsumer) {
    this(eventClass, priority, false, eventConsumer);
  }

  /**
   * Create a new listener executor
   *
   * @param eventClass    the class of the event
   * @param eventConsumer the consumer to handle the event
   */
  public ListenerExecutor(Class<E> eventClass, Consumer<E> eventConsumer) {
    this(eventClass, EventPriority.NORMAL, eventConsumer);
  }

  /**
   * Register the executor to the plugin
   *
   * @param plugin   the plugin
   * @param listener the listener
   */
  public void register(Plugin plugin, Listener listener) {
    plugin.getServer().getPluginManager().registerEvent(eventClass, listener, priority, (l, event) -> {
      if (eventClass.isInstance(event) && l == listener) {
        eventConsumer.accept(eventClass.cast(event));
      }
    }, plugin, ignoreCancelled);
  }
}
