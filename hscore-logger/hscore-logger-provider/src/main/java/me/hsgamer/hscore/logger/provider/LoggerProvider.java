package me.hsgamer.hscore.logger.provider;

import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;
import me.hsgamer.hscore.logger.jul.JulLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class LoggerProvider {
  private static final Map<String, Logger> LOGGER_MAP = new ConcurrentHashMap<>();
  private static Function<String, Logger> LOGGER_PROVIDER = JulLogger::new;

  private LoggerProvider() {
    // EMPTY
  }

  /**
   * Set the logger provider
   *
   * @param loggerProvider the logger provider
   */
  public static void setLoggerProvider(Function<String, Logger> loggerProvider) {
    LOGGER_PROVIDER = loggerProvider;
  }

  /**
   * Get the logger
   *
   * @param name the name
   *
   * @return the logger
   */
  public static Logger getLogger(String name) {
    return LOGGER_MAP.computeIfAbsent(name, s -> new Logger() {
      private Logger logger;

      @Override
      public void log(LogLevel level, String message) {
        if (logger == null) {
          logger = LOGGER_PROVIDER.apply(s);
        }
        logger.log(level, message);
      }
    });
  }

  /**
   * Get the logger
   *
   * @param clazz the class
   *
   * @return the logger
   */
  public static Logger getLogger(Class<?> clazz) {
    return getLogger(clazz.getSimpleName());
  }
}
