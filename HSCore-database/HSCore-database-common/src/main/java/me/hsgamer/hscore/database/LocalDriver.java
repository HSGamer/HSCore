package me.hsgamer.hscore.database;

import java.io.File;

/**
 * The driver for local connection
 */
public abstract class LocalDriver implements Driver {
  private final File folder;

  /**
   * Create a new local driver with the default folder
   */
  public LocalDriver() {
    this(new File("."));
  }

  /**
   * Create a new local driver
   *
   * @param folder the folder to store databases
   */
  public LocalDriver(File folder) {
    this.folder = folder;
    if (!folder.exists()) {
      folder.mkdirs();
    }
  }

  /**
   * Get the folder
   *
   * @return the folder
   */
  public File getFolder() {
    return folder;
  }
}
