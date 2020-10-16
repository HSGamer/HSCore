package me.hsgamer.hscore.database.driver;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;

/**
 * A driver for MariaDB
 */
public class MariaDBDriver implements Driver {
  @Override
  public String getClassName() {
    return "org.mariadb.jdbc.Driver";
  }

  @Override
  public String convertURL(Setting setting) {
    return "jdbc:mariadb://" + setting.getHost() + ':' + setting.getPort() + '/' + setting.getDatabaseName();
  }
}
