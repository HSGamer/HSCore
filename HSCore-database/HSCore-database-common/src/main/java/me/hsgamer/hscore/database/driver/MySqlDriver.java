package me.hsgamer.hscore.database.driver;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;

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
