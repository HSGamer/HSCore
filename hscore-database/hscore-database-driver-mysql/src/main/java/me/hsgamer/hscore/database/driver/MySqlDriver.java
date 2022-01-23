package me.hsgamer.hscore.database.driver;

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
    } catch (Error e) {
      try {
        return Class.forName("com.mysql.jdbc.Driver").asSubclass(java.sql.Driver.class);
      } catch (ClassNotFoundException ex) {
        throw new IllegalStateException("Cannot find the driver class", ex);
      }
    }
  }

  @Override
  public String convertURL(Setting setting) {
    return "jdbc:mysql://" + setting.getHost() + ':' + setting.getPort() + '/' + setting.getDatabaseName();
  }
}
