package me.hsgamer.hscore.database.driver.h2;

import me.hsgamer.hscore.database.Setting;

/**
 * A driver for H2 (Server Mode)
 */
public class H2ServerDriver implements H2BaseDriver {
  private final boolean isSSH;

  /**
   * Create a new server driver
   *
   * @param isSSH true if the server is open to SSH, otherwise it is TCP
   */
  public H2ServerDriver(boolean isSSH) {
    this.isSSH = isSSH;
  }

  /**
   * Create a new TCP server driver
   */
  public H2ServerDriver() {
    this(false);
  }

  @Override
  public String getConnectionString(Setting setting) {
    return (isSSH ? "ssh" : "tcp") + "://" + setting.getHost() + ":" + setting.getPort() + "/" + setting.getDatabaseName();
  }
}
