package me.hsgamer.hscore.database;

/**
 * A driver for database connection
 */
public interface Driver {

  /**
   * Get the class name of the driver
   *
   * @return the class name
   */
  String getClassName();

  /**
   * Get the JDBC url from the config
   *
   * @param setting the setting
   * @return the JDBC url
   */
  String convertURL(Setting setting);
}
