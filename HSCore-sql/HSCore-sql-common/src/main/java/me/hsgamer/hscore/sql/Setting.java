package me.hsgamer.hscore.sql;

public class Setting {
  private String hostName = "";
  private String databaseName = "";
  private String port = "";
  private String username = "";
  private String password = "";
  private boolean useSSL = false;
  private int minConnect = 0;
  private int maxConnect = 100;

  public String getHostName() {
    return hostName;
  }

  public Setting setHostName(String hostName) {
    this.hostName = hostName;
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

  public int getMinConnect() {
    return minConnect;
  }

  public Setting setMinConnect(int minConnect) {
    this.minConnect = minConnect;
    return this;
  }

  public int getMaxConnect() {
    return maxConnect;
  }

  public Setting setMaxConnect(int maxConnect) {
    this.maxConnect = maxConnect;
    return this;
  }
}
