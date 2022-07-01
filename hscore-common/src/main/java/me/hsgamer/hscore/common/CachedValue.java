package me.hsgamer.hscore.common;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The base class for caching value
 *
 * @param <T> the type of the value
 */
public abstract class CachedValue<T> {
  /**
   * The cached value
   */
  private final AtomicReference<T> cache = new AtomicReference<>();
  /**
   * The status to check if the value is cached
   */
  private final AtomicBoolean isCached = new AtomicBoolean(false);

  /**
   * Get the cached value or generate one if the cache is null
   *
   * @return the value
   */
  public T getValue() {
    if (isCached.get()) {
      return cache.get();
    } else {
      T value = generate();
      cache.set(value);
      isCached.set(true);
      return value;
    }
  }

  /**
   * Clear the cached value
   */
  public void clearCache() {
    cache.set(null);
    isCached.set(false);
  }

  /**
   * Generate the value
   *
   * @return the value
   */
  public abstract T generate();
}
