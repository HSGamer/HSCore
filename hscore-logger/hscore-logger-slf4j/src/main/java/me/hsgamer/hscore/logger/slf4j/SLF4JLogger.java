package me.hsgamer.hscore.logger.slf4j;

import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link Logger} for {@link org.slf4j.Logger}
 */
public class SLF4JLogger implements Logger {
  private final org.slf4j.Logger logger;

  /**
   * Create a new logger
   *
   * @param logger the logger
   */
  public SLF4JLogger(org.slf4j.Logger logger) {
    this.logger = logger;
  }

  /**
   * Create a new logger
   *
   * @param name the name
   */
  public SLF4JLogger(String name) {
    this(LoggerFactory.getLogger(name));
  }

  /**
   * Create a new logger
   *
   * @param clazz the class
   */
  public SLF4JLogger(Class<?> clazz) {
    this(LoggerFactory.getLogger(clazz));
  }

  @Override
  public void log(LogLevel level, String message) {
    switch (level) {
      case DEBUG:
        logger.debug(message);
        break;
      case INFO:
        logger.info(message);
        break;
      case WARN:
        logger.warn(message);
        break;
      case ERROR:
        logger.error(message);
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
        logger.debug(message, throwable);
        break;
      case INFO:
        logger.info(message, throwable);
        break;
      case WARN:
        logger.warn(message, throwable);
        break;
      case ERROR:
        logger.error(message, throwable);
        break;
      default:
        break;
    }
  }
}
