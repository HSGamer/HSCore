package me.hsgamer.hscore.database.driver.sqlite;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;
import org.sqlite.JDBC;

/**
 * A driver for SQLite
 */
public interface SqliteBaseDriver extends Driver {
  @Override
  default Class<? extends java.sql.Driver> getDriverClass() {
    return JDBC.class;
  }

  @Override
  default String convertURL(Setting setting) {
    return JDBC.PREFIX + createConnectionString(setting) + Driver.createPropertyString(setting);
  }

  String createConnectionString(Setting setting);
}
