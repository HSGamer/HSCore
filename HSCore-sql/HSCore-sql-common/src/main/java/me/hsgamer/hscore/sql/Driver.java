package me.hsgamer.hscore.sql;

/**
 * A driver for database connection
 */
public interface Driver {

  /**
   * Get the name of the driver
   *
   * @return the name
   */
  String getName();

  /**
   * Get the JDBC url from the config
   *
   * @param setting the setting
   * @return the JDBC url
   */
  String convertURL(Setting setting);
}
