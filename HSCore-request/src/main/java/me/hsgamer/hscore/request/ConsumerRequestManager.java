package me.hsgamer.hscore.request;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

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
  public void addRequest(UUID uuid, BiConsumer<UUID, T> consumer) {
    cache.put(uuid, consumer);
  }

  /**
   * Apply a request
   *
   * @param uuid the unique id
   * @param arg  the argument
   */
  public void apply(UUID uuid, T arg) {
    if (contains(uuid)) {
      cache.remove(uuid).accept(uuid, arg);
    }
  }

  /**
   * Remove a request
   *
   * @param uuid the unique id
   */
  public void remove(UUID uuid) {
    cache.remove(uuid);
  }

  /**
   * Check if a unique id is in the request cache
   *
   * @param uuid the unique id
   * @return whether it's in the request cache
   */
  public boolean contains(UUID uuid) {
    return cache.containsKey(uuid);
  }
}
