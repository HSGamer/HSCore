package me.hsgamer.hscore.sql;

import java.io.File;

/**
 * The driver for local connection
 */
public abstract class LocalDriver implements Driver {
  private final File folder;

  /**
   * Create a new local driver
   *
   * @param folder the folder to store databases
   */
  public LocalDriver(File folder) {
    this.folder = folder;
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
