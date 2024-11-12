package me.hsgamer.hscore.ui;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The base implementation for {@link UI}
 */
public abstract class BaseUI implements UI {
  private final List<Predicate<Object>> eventConsumerList = new ArrayList<>();

  /**
   * Add an event consumer
   *
   * @param eventConsumer the consumer, return true to continue the event handling
   */
  public void addEventConsumer(@NotNull Predicate<Object> eventConsumer) {
    eventConsumerList.add(eventConsumer);
  }

  /**
   * Add an event consumer
   *
   * @param eventConsumer the consumer
   */
  public void addEventConsumer(@NotNull Consumer<Object> eventConsumer) {
    addEventConsumer(event -> {
      eventConsumer.accept(event);
      return true;
    });
  }

  /**
   * Add an event consumer
   *
   * @param eventClass    the class of the event
   * @param eventConsumer the consumer, return true to continue the event handling
   * @param <T>           the type of the event
   *
   * @return the registered consumer
   */
  public <T> Predicate<Object> addEventConsumer(@NotNull Class<T> eventClass, @NotNull Predicate<T> eventConsumer) {
    Predicate<Object> consumer = event -> !eventClass.isInstance(event) || eventConsumer.test(eventClass.cast(event));
    addEventConsumer(consumer);
    return consumer;
  }

  /**
   * Add an event consumer
   *
   * @param eventClass    the class of the event
   * @param eventConsumer the consumer
   * @param <T>           the type of the event
   */
  public <T> Predicate<Object> addEventConsumer(@NotNull Class<T> eventClass, @NotNull Consumer<T> eventConsumer) {
    return addEventConsumer(eventClass, event -> {
      eventConsumer.accept(event);
      return true;
    });
  }

  /**
   * Remove an event consumer
   *
   * @param eventConsumer the consumer
   */
  public void removeEventConsumer(@NotNull Predicate<Object> eventConsumer) {
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
    for (Predicate<Object> eventConsumer : eventConsumerList) {
      if (!eventConsumer.test(event)) {
        break;
      }
    }
  }
}
