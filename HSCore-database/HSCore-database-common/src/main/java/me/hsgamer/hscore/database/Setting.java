package me.hsgamer.hscore.database;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The setting for connection
 */
public class Setting {
  private final HashMap<String, Object> properties = new HashMap<>();
  private String host = "localhost";
  private String databaseName = "db";
  private String port = "3306";
  private String username = "";
  private String password = "";

  /**
   * Get the host
   *
   * @return the host
   */
  public String getHost() {
    return this.host;
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
    return this.databaseName;
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
    return this.port;
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
    return this.username;
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
    return this.password;
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
   * Set the property
   *
   * @param property the property
   * @param value    the value
   */
  public Setting setProperty(String property, Object value) {
    this.properties.put(property, value);
    return this;
  }

  /**
   * Get the property
   *
   * @param property the property
   *
   * @return the value
   */
  public Object getProperty(String property) {
    return this.properties.get(property);
  }

  /**
   * Get all properties
   *
   * @return the properties
   */
  public Map<String, Object> getProperties() {
    return Collections.unmodifiableMap(properties);
  }
}
