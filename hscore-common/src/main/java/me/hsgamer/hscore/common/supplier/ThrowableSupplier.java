package me.hsgamer.hscore.common.supplier;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

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
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "There is an exception on getting", throwable);
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
