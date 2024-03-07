package me.hsgamer.hscore.database.driver.h2;

import me.hsgamer.hscore.database.LocalDriver;
import me.hsgamer.hscore.database.Setting;

import java.io.File;
import java.nio.file.Paths;

/**
 * The driver for H2 (Embedded Mode)
 */
public class H2LocalDriver extends LocalDriver implements H2BaseDriver {
  public H2LocalDriver() {
    super();
  }

  public H2LocalDriver(File folder) {
    super(folder);
  }

  @Override
  public String getConnectionString(Setting setting) {
    return Paths.get(getFolder().getAbsolutePath(), setting.getDatabaseName()).toString();
  }
}
