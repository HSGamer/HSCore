package me.hsgamer.hscore.ui;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * The base implementation for {@link UI}
 */
public abstract class BaseUI implements UI {
  private final LinkedList<Consumer<Object>> eventConsumerList = new LinkedList<>();

  /**
   * Add an event consumer
   *
   * @param eventConsumer the consumer
   */
  public void addEventConsumer(@NotNull Consumer<Object> eventConsumer) {
    eventConsumerList.add(eventConsumer);
  }

  /**
   * Add an event consumer
   *
   * @param eventClass    the class of the event
   * @param eventConsumer the consumer
   * @param <T>           the type of the event
   */
  public <T> Consumer<Object> addEventConsumer(@NotNull Class<T> eventClass, @NotNull Consumer<T> eventConsumer) {
    Consumer<Object> consumer = event -> {
      if (eventClass.isInstance(event)) {
        eventConsumer.accept(eventClass.cast(event));
      }
    };
    addEventConsumer(consumer);
    return consumer;
  }

  /**
   * Remove an event consumer
   *
   * @param eventConsumer the consumer
   */
  public void removeEventConsumer(@NotNull Consumer<Object> eventConsumer) {
    eventConsumerList.remove(eventConsumer);
  }

  /**
   * Clear all event consumers
   */
  public void clearAllEventConsumer() {
    eventConsumerList.clear();
  }

  @Override
  public void stop() {
    clearAllEventConsumer();
  }

  @Override
  public void handleEvent(@NotNull Object event) {
    eventConsumerList.forEach(consumer -> consumer.accept(event));
  }
}
