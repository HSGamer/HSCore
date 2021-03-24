package me.hsgamer.hscore.common.function;

import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link Function} but returns null if there is an exception
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 */
public interface ThrowableFunction<T, R> extends Function<T, R> {
  @Override
  default R apply(T t) {
    try {
      return applySafe(t);
    } catch (Throwable throwable) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "There is an exception on applying", throwable);
      return null;
    }
  }

  /**
   * Apply with throwable
   *
   * @param t the input
   *
   * @return the result
   *
   * @throws Throwable the exception
   */
  R applySafe(T t) throws Throwable;
}
