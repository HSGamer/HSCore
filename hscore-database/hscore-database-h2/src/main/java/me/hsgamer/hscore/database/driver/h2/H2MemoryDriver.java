package me.hsgamer.hscore.database.driver.h2;

import me.hsgamer.hscore.database.Setting;

/**
 * A driver for H2 (In-Memory Mode)
 */
public class H2MemoryDriver implements H2BaseDriver {
  @Override
  public String getConnectionString(Setting setting) {
    return "mem:" + setting.getDatabaseName();
  }
}
