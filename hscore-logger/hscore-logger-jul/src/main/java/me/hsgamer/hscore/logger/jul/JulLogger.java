package me.hsgamer.hscore.logger.jul;

import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;

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
}
