package me.hsgamer.hscore.common.consumer;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link Consumer} but ignores exceptions
 *
 * @param <T> the type of the input to the operation
 */
public interface ThrowableConsumer<T> extends Consumer<T> {
  @Override
  default void accept(T t) {
    try {
      acceptSafe(t);
    } catch (Throwable throwable) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "There is an exception on accepting", throwable);
    }
  }

  /**
   * Accept with exception
   *
   * @param t the input
   *
   * @throws Throwable the exception
   */
  void acceptSafe(T t) throws Throwable;
}
