package me.hsgamer.hscore.database.client.sql.java;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.client.sql.SqlClient;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The SQL client with the Java's Driver Manager
 */
public class JavaSqlClient implements SqlClient<Properties> {

  private final Setting setting;
  private final Properties properties;
  private final String dbURL;

  /**
   * Create new SQL client
   *
   * @param setting the setting
   * @param driver  the driver
   */
  public JavaSqlClient(Setting setting, Driver driver) {
    this.setting = setting;
    this.properties = new Properties();
    try {
      java.sql.Driver javaDriver = driver.getDriverClass().getConstructor().newInstance();
      DriverManager.registerDriver(javaDriver);
    } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
      throw new IllegalStateException("Failed to register the driver", e);
    }
    properties.setProperty("user", setting.getUsername());
    properties.setProperty("password", setting.getPassword());
    setting.getProperties().forEach((k, v) -> properties.setProperty(k, String.valueOf(v)));
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

  @Override
  public Setting getSetting() {
    return setting;
  }
}
