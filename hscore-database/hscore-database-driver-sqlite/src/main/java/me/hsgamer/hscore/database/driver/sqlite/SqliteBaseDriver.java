package me.hsgamer.hscore.database.driver.sqlite;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;

/**
 * A driver for SQLite
 */
public interface SqliteBaseDriver extends Driver {
  @Override
  default Class<? extends java.sql.Driver> getDriverClass() {
    return org.sqlite.JDBC.class;
  }

  @Override
  default String convertURL(Setting setting) {
    return "jdbc:sqlite:" + createConnectionString(setting);
  }

  String createConnectionString(Setting setting);
}
