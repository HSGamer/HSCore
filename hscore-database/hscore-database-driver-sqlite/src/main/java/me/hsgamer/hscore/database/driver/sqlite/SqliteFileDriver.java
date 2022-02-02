package me.hsgamer.hscore.database.driver.sqlite;

import me.hsgamer.hscore.database.LocalDriver;
import me.hsgamer.hscore.database.Setting;

import java.io.File;
import java.nio.file.Paths;

/**
 * A driver for SQLite (File Mode)
 */
public class SqliteFileDriver extends LocalDriver implements SqliteBaseDriver {
  public SqliteFileDriver() {
    super();
  }

  public SqliteFileDriver(File folder) {
    super(folder);
  }

  @Override
  public String createConnectionString(Setting setting) {
    return Paths.get(getFolder().getAbsolutePath(), setting.getDatabaseName() + ".db").toString();
  }
}
