package me.hsgamer.hscore.database;

/**
 * A driver for database connection
 */
public interface Driver {

  /**
   * Get the driver class
   *
   * @return the driver class
   */
  Class<? extends java.sql.Driver> getDriverClass();

  /**
   * Get the JDBC url from the config
   *
   * @param setting the setting
   *
   * @return the JDBC url
   */
  String convertURL(Setting setting);
}
