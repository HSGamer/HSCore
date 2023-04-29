package me.hsgamer.hscore.database.client.sql.java;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.client.sql.BaseSqlClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The SQL client with the Java's Driver Manager
 */
public class JavaSqlClient extends BaseSqlClient<Properties> {
  private final Properties properties;
  private final String dbURL;
  private final java.sql.Driver sqlDriver;

  /**
   * Create new SQL client
   *
   * @param setting the setting
   */
  public JavaSqlClient(Setting setting) {
    super(setting);
    Driver driver = setting.getDriver();
    this.properties = new Properties();
    try {
      this.sqlDriver = driver.getDriverClass().getConstructor().newInstance();
    } catch (Exception e) {
      throw new IllegalStateException("Failed to load the driver", e);
    }
    properties.setProperty("user", setting.getUsername());
    properties.setProperty("password", setting.getPassword());
    setting.getClientProperties().forEach((k, v) -> properties.setProperty(k, String.valueOf(v)));
    this.dbURL = driver.convertURL(setting);
  }

  @Override
  public Connection getConnection() throws SQLException {
    try {
      return sqlDriver.connect(dbURL, properties);
    } catch (SecurityException | IllegalArgumentException ex) {
      return DriverManager.getConnection(dbURL, properties);
    }
  }

  @Override
  public Properties getOriginal() {
    return properties;
  }
}
