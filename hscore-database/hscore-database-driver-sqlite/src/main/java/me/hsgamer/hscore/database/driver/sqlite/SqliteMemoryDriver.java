package me.hsgamer.hscore.database.driver.sqlite;

import me.hsgamer.hscore.database.Setting;

/**
 * A driver for SQLite (In-Memory Mode)
 */
public class SqliteMemoryDriver implements SqliteBaseDriver {
  @Override
  public String createConnectionString(Setting setting) {
    return ":memory:";
  }
}
