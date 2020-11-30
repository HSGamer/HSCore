package me.hsgamer.hscore.common.supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * a class that uses {@link Supplier} and caches it to use after.
 *
 * @param <T> the value's type.
 */
public final class StickySupplier<T> implements Supplier<T> {

  /**
   * the original {@link Supplier}.
   */
  @NotNull
  private final Supplier<T> origin;

  /**
   * the cache value.
   */
  @Nullable
  private T cache;

  /**
   * ctor.
   *
   * @param origin the original {@link Supplier}.
   */
  public StickySupplier(@NotNull final Supplier<T> origin) {
    this.origin = origin;
  }

  /**
   * ctor.
   *
   * @param origin the original {@link T}.
   */
  public StickySupplier(@NotNull final T origin) {
    this(() -> origin);
  }

  @Override
  public T get() {
    return Optional.ofNullable(this.cache).orElseGet(() -> {
      this.cache = this.origin.get();
      return this.cache;
    });
  }
}
