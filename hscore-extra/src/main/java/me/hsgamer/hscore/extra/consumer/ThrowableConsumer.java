package me.hsgamer.hscore.extra.consumer;

import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.provider.LoggerProvider;

import java.util.function.Consumer;

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
      LoggerProvider.getLogger(ThrowableConsumer.class).log(LogLevel.WARN, throwable);
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
