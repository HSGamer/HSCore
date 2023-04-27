package me.hsgamer.hscore.logger.system;

import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;

/**
 * The system {@link Logger}
 */
public class SystemLogger implements Logger {
  private final String name;
  private boolean debug = false;

  /**
   * Create a new logger
   *
   * @param name the name
   */
  public SystemLogger(String name) {
    this.name = name;
  }

  /**
   * Create a new logger
   *
   * @param clazz the class
   */
  public SystemLogger(Class<?> clazz) {
    this(clazz.getSimpleName());
  }

  /**
   * Enable printing debug messages
   *
   * @return this instance
   */
  public SystemLogger printDebug() {
    this.debug = true;
    return this;
  }

  @Override
  public void log(LogLevel level, String message) {
    if (level == LogLevel.DEBUG && !debug) {
      return;
    }

    if (level == LogLevel.ERROR) {
      System.err.println("[" + name + "/" + level.name() + "] " + message);
    } else {
      System.out.println("[" + name + "/" + level.name() + "] " + message);
    }
  }
}
