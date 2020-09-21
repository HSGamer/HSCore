package me.hsgamer.hscore.request;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A simple request manager
 *
 * @param <T> the type of the argument
 */
public class ConsumerRequestManager<T> {

  private final Map<UUID, BiConsumer<UUID, T>> cache = new ConcurrentHashMap<>();

  /**
   * Add a request
   *
   * @param uuid     the unique id
   * @param consumer the consumer when the request is called
   */
  public void addRequest(@NotNull UUID uuid, @NotNull BiConsumer<UUID, T> consumer) {
    cache.put(uuid, consumer);
  }

  /**
   * Add a request
   *
   * @param uuid     the unique id
   * @param consumer the consumer when the request is called
   */
  public void addRequest(@NotNull UUID uuid, @NotNull Consumer<T> consumer) {
    addRequest(uuid, (uuid1, t) -> consumer.accept(t));
  }

  /**
   * Apply a request
   *
   * @param uuid the unique id
   * @param arg  the argument
   */
  public void apply(@NotNull UUID uuid, @NotNull T arg) {
    if (contains(uuid)) {
      cache.remove(uuid).accept(uuid, arg);
    }
  }

  /**
   * Remove a request
   *
   * @param uuid the unique id
   */
  public void remove(@NotNull UUID uuid) {
    cache.remove(uuid);
  }

  /**
   * Check if a unique id is in the request cache
   *
   * @param uuid the unique id
   * @return whether it's in the request cache
   */
  public boolean contains(@NotNull UUID uuid) {
    return cache.containsKey(uuid);
  }
}
