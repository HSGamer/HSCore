package me.hsgamer.hscore.database.driver.mysql;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;

/**
 * A driver for MySQL
 */
public class MySqlDriver implements Driver {

  @Override
  public Class<? extends java.sql.Driver> getDriverClass() {
    try {
      return com.mysql.cj.jdbc.Driver.class;
    } catch (NoClassDefFoundError | ExceptionInInitializerError e) {
      try {
        return Class.forName("com.mysql.jdbc.Driver").asSubclass(java.sql.Driver.class);
      } catch (ClassNotFoundException ex) {
        throw new IllegalStateException("Cannot find the driver class", ex);
      }
    }
  }

  @Override
  public String convertURL(Setting setting) {
    return "jdbc:mysql://" + setting.getHost() + ':' + setting.getPort() + '/' + setting.getDatabaseName() + Driver.createPropertyString(setting);
  }

  @Override
  public Setting applyDefaultSetting(Setting setting) {
    return setting.setPort("3306").setUsername("root");
  }
}
