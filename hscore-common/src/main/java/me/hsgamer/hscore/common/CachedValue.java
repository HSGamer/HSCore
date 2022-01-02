package me.hsgamer.hscore.common;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * The base class for caching value
 *
 * @param <T> the type of the value
 */
public abstract class CachedValue<T> {
  /**
   * The cached value
   */
  @Nullable
  private T cache;

  /**
   * Get the cached value or generate one if the cache is null
   *
   * @return the value
   */
  public T getValue() {
    return Optional.ofNullable(this.cache).orElseGet(() -> {
      this.cache = this.generate();
      return this.cache;
    });
  }

  /**
   * Clear the cached value
   */
  public void clearCache() {
    this.cache = null;
  }

  /**
   * Generate the value
   *
   * @return the value
   */
  public abstract T generate();
}
