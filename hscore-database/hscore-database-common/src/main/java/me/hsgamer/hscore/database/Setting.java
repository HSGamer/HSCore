package me.hsgamer.hscore.database;

import org.jetbrains.annotations.Contract;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The setting for connection
 */
public class Setting {
  private final Driver driver;
  private final Map<String, Object> clientProperties;
  private final Map<String, Object> driverProperties;
  private String host;
  private String databaseName;
  private String port;
  private String username;
  private String password;

  /**
   * The constructor
   */
  private Setting(Driver driver) {
    this.driver = driver;
    clientProperties = new HashMap<>();
    driverProperties = new HashMap<>();
    host = "localhost";
    databaseName = "";
    port = "";
    username = "root";
    password = "";
    driver.applyDefaultSetting(this);
  }

  /**
   * Create a new setting with the default values from the driver
   *
   * @param driver the driver
   *
   * @return the setting
   */
  public static Setting create(Driver driver) {
    return new Setting(driver);
  }

  /**
   * Deserialize the setting from the map
   *
   * @param map the map
   *
   * @return the setting
   */
  public static Setting deserialize(Driver driver, Map<String, Object> map) {
    Setting setting = new Setting(driver);
    setting.host = (String) map.get("host");
    setting.databaseName = (String) map.get("databaseName");
    setting.port = (String) map.get("port");
    setting.username = (String) map.get("username");
    setting.password = (String) map.get("password");
    //noinspection unchecked
    setting.clientProperties.putAll((Map<String, Object>) map.get("clientProperties"));
    //noinspection unchecked
    setting.driverProperties.putAll((Map<String, Object>) map.get("driverProperties"));
    return setting;
  }

  /**
   * Serialize the setting to the map
   *
   * @return the map
   */
  public Map<String, Object> serialize() {
    Map<String, Object> map = new HashMap<>();
    map.put("host", host);
    map.put("databaseName", databaseName);
    map.put("port", port);
    map.put("username", username);
    map.put("password", password);
    map.put("clientProperties", clientProperties);
    map.put("driverProperties", driverProperties);
    return map;
  }

  /**
   * Get the driver
   *
   * @return the driver
   */
  public Driver getDriver() {
    return driver;
  }

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
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
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
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
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
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
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
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
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
   *
   * @return {@code this} for builder chain
   */
  @Contract("_ -> this")
  public Setting setPassword(String password) {
    this.password = password;
    return this;
  }

  /**
   * Set the client property
   *
   * @param property the property
   * @param value    the value
   */
  @Contract("_, _ -> this")
  public Setting setClientProperty(String property, Object value) {
    this.clientProperties.put(property, value);
    return this;
  }

  /**
   * Get all client properties
   *
   * @return the properties
   */
  public Map<String, Object> getClientProperties() {
    return Collections.unmodifiableMap(clientProperties);
  }

  /**
   * Set the client properties
   *
   * @param properties the properties
   */
  @Contract("_ -> this")
  public Setting setClientProperties(Map<String, Object> properties) {
    this.clientProperties.putAll(properties);
    return this;
  }

  /**
   * Get all driver properties as a list of string
   *
   * @return the properties as a list of string
   */
  public List<String> getClientPropertyStrings() {
    return Collections.unmodifiableList(clientProperties.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.toList()));
  }

  /**
   * Set the driver property
   *
   * @param property the property
   * @param value    the value
   */
  @Contract("_, _ -> this")
  public Setting setDriverProperty(String property, Object value) {
    this.driverProperties.put(property, value);
    return this;
  }

  /**
   * Get all driver properties
   *
   * @return the properties
   */
  public Map<String, Object> getDriverProperties() {
    return Collections.unmodifiableMap(driverProperties);
  }

  /**
   * Set the driver properties
   *
   * @param properties the properties
   */
  @Contract("_ -> this")
  public Setting setDriverProperties(Map<String, Object> properties) {
    this.driverProperties.putAll(properties);
    return this;
  }

  /**
   * Get all driver properties as a list of string
   *
   * @return the properties as a list of string
   */
  public List<String> getDriverPropertyStrings() {
    return Collections.unmodifiableList(driverProperties.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.toList()));
  }

  /**
   * Get the host and port as a base url
   *
   * @return the base url
   */
  public String getNormalizedHost() {
    return host + (port.isEmpty() ? "" : ":" + port);
  }
}
