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

  /**
   * Create new SQL client
   *
   * @param setting the setting
   * @param driver  the driver
   */
  public JavaSqlClient(Setting setting, Driver driver) {
    super(setting);
    this.properties = new Properties();
    try {
      Class.forName(driver.getDriverClass().getName());
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Failed to load the driver", e);
    }
    properties.setProperty("user", setting.getUsername());
    properties.setProperty("password", setting.getPassword());
    setting.getClientProperties().forEach((k, v) -> properties.setProperty(k, String.valueOf(v)));
    this.dbURL = driver.convertURL(setting);
  }

  @Override
  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(dbURL, properties);
  }

  @Override
  public Properties getOriginal() {
    return properties;
  }
}
