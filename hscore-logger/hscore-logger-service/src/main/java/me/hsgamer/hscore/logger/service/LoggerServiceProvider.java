package me.hsgamer.hscore.logger.service;

import me.hsgamer.hscore.logger.system.SystemLogger;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * The service provider for {@link LoggerService}
 */
public final class LoggerServiceProvider {
  /**
   * The service
   */
  private static LoggerService service;

  private LoggerServiceProvider() {
    // EMPTY
  }

  /**
   * Get the service
   *
   * @return the service
   */
  public static LoggerService getService() {
    if (service == null) {
      Iterator<LoggerService> iterator = ServiceLoader.load(LoggerService.class).iterator();
      if (iterator.hasNext()) {
        service = iterator.next();
      } else {
        System.out.println("No LoggerService found, using SystemLogger");
        service = SystemLogger::new;
      }
    }
    return service;
  }
}
