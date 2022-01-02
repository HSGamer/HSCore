package me.hsgamer.hscore.database.driver;

import me.hsgamer.hscore.database.LocalDriver;
import me.hsgamer.hscore.database.Setting;

import java.io.File;
import java.nio.file.Paths;

/**
 * A driver for SQLite
 */
public class SqliteDriver extends LocalDriver {
  public SqliteDriver() {
    super();
  }

  public SqliteDriver(File folder) {
    super(folder);
  }

  @Override
  public Class<? extends java.sql.Driver> getDriverClass() {
    return org.sqlite.JDBC.class;
  }

  @Override
  public String convertURL(Setting setting) {
    return "jdbc:sqlite:" + Paths.get(getFolder().getAbsolutePath(), setting.getDatabaseName() + ".db");
  }
}
