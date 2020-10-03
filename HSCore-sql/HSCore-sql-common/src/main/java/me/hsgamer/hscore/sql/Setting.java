package me.hsgamer.hscore.sql;

public class Setting {
  private String host = "";
  private String databaseName = "";
  private String port = "";
  private String username = "";
  private String password = "";
  private String driver = "";
  private boolean useSSL = false;

  public String getHost() {
    return host;
  }

  public Setting setHost(String host) {
    this.host = host;
    return this;
  }

  public String getDatabaseName() {
    return databaseName;
  }

  public Setting setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
    return this;
  }

  public String getPort() {
    return port;
  }

  public Setting setPort(String port) {
    this.port = port;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public Setting setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public Setting setPassword(String password) {
    this.password = password;
    return this;
  }

  public boolean isUseSSL() {
    return useSSL;
  }

  public Setting setUseSSL(boolean useSSL) {
    this.useSSL = useSSL;
    return this;
  }

  public String getDriver() {
    return driver;
  }

  public Setting setDriver(String driver) {
    this.driver = driver;
    return this;
  }
}
