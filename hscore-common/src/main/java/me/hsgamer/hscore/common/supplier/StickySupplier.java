package me.hsgamer.hscore.common.supplier;

import me.hsgamer.hscore.common.CachedValue;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * a class that uses {@link Supplier} and caches it to use after.
 *
 * @param <T> the value's type.
 */
public final class StickySupplier<T> extends CachedValue<T> implements Supplier<T> {
  /**
   * the original {@link Supplier}.
   */
  @NotNull
  private final Supplier<T> origin;

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
    return this.getValue();
  }

  @Override
  public T generate() {
    return this.origin.get();
  }
}
