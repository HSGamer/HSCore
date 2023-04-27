package me.hsgamer.hscore.common.function;

import java.util.function.Function;

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
      throwable.printStackTrace();
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
