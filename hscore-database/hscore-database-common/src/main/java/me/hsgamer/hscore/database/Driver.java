package me.hsgamer.hscore.database;

import java.util.List;

/**
 * A driver for database connection
 */
public interface Driver {

  /**
   * Create the property string for the URL
   *
   * @param setting   the setting
   * @param prefix    the prefix
   * @param delimiter the delimiter between the properties
   *
   * @return the property string
   */
  static String createPropertyString(Setting setting, String prefix, String delimiter) {
    StringBuilder builder = new StringBuilder();
    List<String> properties = setting.getDriverPropertyStrings();
    if (!properties.isEmpty()) {
      builder.append(prefix);
      builder.append(String.join(delimiter, properties));
    }
    return builder.toString();
  }

  /**
   * Create the property string for the URL
   *
   * @param setting the setting
   *
   * @return the property string
   */
  static String createPropertyString(Setting setting) {
    return createPropertyString(setting, "?", "&");
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

  /**
   * Apply the default setting
   *
   * @param setting the setting
   */
  default void applyDefaultSetting(Setting setting) {
    // EMPTY
  }
}
