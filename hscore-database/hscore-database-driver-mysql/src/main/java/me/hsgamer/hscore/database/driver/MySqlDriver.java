package me.hsgamer.hscore.database.driver;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;

/**
 * A driver for MySQL
 */
public class MySqlDriver implements Driver {

  @Override
  public Class<? extends java.sql.Driver> getDriverClass() {
    return com.mysql.cj.jdbc.Driver.class;
  }

  @Override
  public String convertURL(Setting setting) {
    return "jdbc:mysql://" + setting.getHost() + ':' + setting.getPort() + '/' + setting.getDatabaseName();
  }
}
