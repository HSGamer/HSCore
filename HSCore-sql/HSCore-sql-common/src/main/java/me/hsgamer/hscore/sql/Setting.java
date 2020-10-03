package me.hsgamer.hscore.sql;

/**
 * The setting for connection
 */
public class Setting {
  private String host = "";
  private String databaseName = "";
  private String port = "";
  private String username = "";
  private String password = "";
  private String driver = "";
  private boolean useSSL = false;

  /**
   * Get the host
   *
   * @return the host
   */
  public String getHost() {
    return host;
  }

  /**
   * Set the host
   *
   * @param host the host
   */
  public Setting setHost(String host) {
    this.host = host;
    return this;
  }

  /**
   * Get the database name
   *
   * @return the database name
   */
  public String getDatabaseName() {
    return databaseName;
  }

  /**
   * Set the database name
   *
   * @param databaseName the database name
   */
  public Setting setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
    return this;
  }

  /**
   * Get the port
   *
   * @return the port
   */
  public String getPort() {
    return port;
  }

  /**
   * Set the port
   *
   * @param port the port
   */
  public Setting setPort(String port) {
    this.port = port;
    return this;
  }

  /**
   * Get the username
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Set the username
   *
   * @param username the username
   */
  public Setting setUsername(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get the password
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Set the password
   *
   * @param password the password
   */
  public Setting setPassword(String password) {
    this.password = password;
    return this;
  }

  /**
   * Should it use SSL?
   *
   * @return the boolean
   */
  public boolean isUseSSL() {
    return useSSL;
  }

  /**
   * Should it use SSL?
   *
   * @param useSSL the boolean
   */
  public Setting setUseSSL(boolean useSSL) {
    this.useSSL = useSSL;
    return this;
  }

  /**
   * Get the driver class
   *
   * @return the driver class
   */
  public String getDriver() {
    return driver;
  }

  /**
   * Set the driver class
   *
   * @return the driver class
   */
  public Setting setDriver(String driver) {
    this.driver = driver;
    return this;
  }
}
