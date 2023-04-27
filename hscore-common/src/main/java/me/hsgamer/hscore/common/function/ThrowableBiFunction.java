package me.hsgamer.hscore.common.function;

import java.util.function.BiFunction;

/**
 * {@link BiFunction} but returns null if there is an exception
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 */
public interface ThrowableBiFunction<T, U, R> extends BiFunction<T, U, R> {
  @Override
  default R apply(T t, U u) {
    try {
      return applySafe(t, u);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return null;
    }
  }

  /**
   * Apply with throwable
   *
   * @param t the first value
   * @param u the second value
   *
   * @return the result value
   *
   * @throws Throwable the exception
   */
  R applySafe(T t, U u) throws Throwable;
}
