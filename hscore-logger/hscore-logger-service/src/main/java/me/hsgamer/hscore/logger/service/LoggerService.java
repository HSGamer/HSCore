package me.hsgamer.hscore.logger.service;

import me.hsgamer.hscore.logger.common.Logger;

/**
 * The service to get the logger
 */
public interface LoggerService {
  /**
   * Get the logger
   *
   * @param name the name of the logger
   *
   * @return the logger
   */
  Logger getLogger(String name);
}
