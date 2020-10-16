package me.hsgamer.hscore.database.driver;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;

/**
 * A driver for PostgreSQL
 */
public class PostgreSqlDriver implements Driver {
  @Override
  public String getClassName() {
    return "org.postgresql.Driver";
  }

  @Override
  public String convertURL(Setting setting) {
    return "jdbc:postgresql://" + setting.getHost() + ':' + setting.getPort() + '/' + setting.getDatabaseName();
  }
}
