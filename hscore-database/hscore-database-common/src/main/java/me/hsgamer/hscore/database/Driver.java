package me.hsgamer.hscore.database;

/**
 * A driver for database connection
 */
public interface Driver {

  /**
   * Create the property string for the URL
   *
   * @param setting the setting
   *
   * @return the property string
   */
  static String createPropertyString(Setting setting) {
    StringBuilder builder = new StringBuilder();
    if (setting.getDriverProperties() != null) {
      builder.append("?");
      setting.getDriverProperties().forEach((key, value) -> builder.append(key).append("=").append(value).append("&"));
    }
    return builder.toString();
  }

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
