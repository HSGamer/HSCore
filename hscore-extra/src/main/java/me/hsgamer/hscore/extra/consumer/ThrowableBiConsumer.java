package me.hsgamer.hscore.extra.consumer;

import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.provider.LoggerProvider;

import java.util.function.BiConsumer;

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
      LoggerProvider.getLogger(ThrowableBiConsumer.class).log(LogLevel.WARN, throwable);
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
