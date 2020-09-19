package me.hsgamer.hscore.request;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A simple request manager with return values
 *
 * @param <T> the type of the argument
 * @param <V> the type of the return value
 */
public class FunctionRequestManager<T, V> {

  private final Map<UUID, BiFunction<UUID, T, V>> cache = new ConcurrentHashMap<>();

  /**
   * Add a request
   *
   * @param uuid     the unique id
   * @param function the function when the request is called
   */
  public void addRequest(UUID uuid, BiFunction<UUID, T, V> function) {
    cache.put(uuid, function);
  }

  /**
   * Add a request
   *
   * @param uuid     the unique id
   * @param function the function when the request is called
   */
  public void addRequest(UUID uuid, Function<T, V> function) {
    addRequest(uuid, (uuid1, t) -> function.apply(t));
  }

  /**
   * Apply a request
   *
   * @param uuid the unique id
   * @param arg  the argument
   * @return the return value from the function
   */
  public V apply(UUID uuid, T arg) {
    if (cache.containsKey(uuid)) {
      return cache.remove(uuid).apply(uuid, arg);
    }
    return null;
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
