package me.hsgamer.hscore.common.consumer;

import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link BiConsumer} but ignores exceptions
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 */
public interface ThrowableBiConsumer<T, U> extends BiConsumer<T, U> {
  @Override
  default void accept(T t, U u) {
    try {
      acceptSafe(t, u);
    } catch (Throwable throwable) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "There is an exception on accepting", throwable);
    }
  }

  /**
   * Accept with exception
   *
   * @param t the first value
   * @param u the second value
   *
   * @throws Throwable the exception
   */
  void acceptSafe(T t, U u) throws Throwable;
}
