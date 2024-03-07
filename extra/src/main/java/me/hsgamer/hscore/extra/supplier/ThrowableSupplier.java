package me.hsgamer.hscore.extra.supplier;

import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.provider.LoggerProvider;

import java.util.function.Supplier;

/**
 * {@link Supplier} but returns null if there is an exception
 *
 * @param <T> the type of results supplied by this supplier
 */
public interface ThrowableSupplier<T> extends Supplier<T> {
  @Override
  default T get() {
    try {
      return getSafe();
    } catch (Throwable throwable) {
      LoggerProvider.getLogger(ThrowableSupplier.class).log(LogLevel.WARN, throwable);
      return null;
    }
  }

  /**
   * Get a result with exceptions
   *
   * @return a result
   *
   * @throws Throwable the exception
   */
  T getSafe() throws Throwable;
}
