package me.hsgamer.hscore.sql.driver;

import me.hsgamer.hscore.sql.Driver;
import me.hsgamer.hscore.sql.Setting;

/**
 * A driver for MySQL
 */
public class MySqlDriver implements Driver {

  @Override
  public String getName() {
    return "com.mysql.cj.jdbc.Driver";
  }

  @Override
  public String convertURL(Setting setting) {
    return "jdbc:mysql://" + setting.getHost() + ':' + setting.getPort() + '/' + setting.getDatabaseName();
  }
}
