package me.hsgamer.hscore.logger.jul;

import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;

import static java.util.logging.Level.*;

/**
 * The {@link Logger} for {@link java.util.logging.Logger}
 */
public class JulLogger implements Logger {
  private final java.util.logging.Logger logger;

  /**
   * Create a new logger
   *
   * @param logger the logger
   */
  public JulLogger(java.util.logging.Logger logger) {
    this.logger = logger;
  }

  /**
   * Create a new logger
   *
   * @param name the name
   */
  public JulLogger(String name) {
    this(java.util.logging.Logger.getLogger(name));
  }

  /**
   * Create a new logger
   *
   * @param clazz the class
   */
  public JulLogger(Class<?> clazz) {
    this(clazz.getSimpleName());
  }

  @Override
  public void log(LogLevel level, String message) {
    switch (level) {
      case DEBUG:
        logger.fine(message);
        break;
      case INFO:
        logger.info(message);
        break;
      case WARN:
        logger.warning(message);
        break;
      case ERROR:
        logger.severe(message);
        break;
      default:
        break;
    }
  }

  @Override
  public void log(LogLevel level, Throwable throwable) {
    log(level, throwable.getMessage(), throwable);
  }

  @Override
  public void log(LogLevel level, String message, Throwable throwable) {
    switch (level) {
      case DEBUG:
        logger.log(FINE, message, throwable);
        break;
      case INFO:
        logger.log(INFO, message, throwable);
        break;
      case WARN:
        logger.log(WARNING, message, throwable);
        break;
      case ERROR:
        logger.log(SEVERE, message, throwable);
        break;
      default:
        break;
    }
  }
}
