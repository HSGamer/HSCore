package me.hsgamer.hscore.database.client.sql.java;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.LocalDriver;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.client.sql.SqlClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * The SQL client with the Java's Driver Manager
 */
public class JavaSqlClient implements SqlClient<Object> {

  private final Setting setting;
  private final String dbURL;

  /**
   * Create new SQL client
   *
   * @param setting the setting
   * @param driver  the driver
   *
   * @throws ClassNotFoundException if the driver is not found
   */
  public JavaSqlClient(Setting setting, Driver driver) throws ClassNotFoundException {
    this.setting = setting;
    Class.forName(driver.getClassName());
    StringBuilder builder = new StringBuilder();
    builder.append(driver.convertURL(setting));
    if (!(driver instanceof LocalDriver)) {
      builder.append("?");
      builder.append(
        setting.getProperties().entrySet()
          .stream()
          .map(entry -> entry.getKey() + "=" + entry.getValue())
          .collect(Collectors.joining("&"))
      );
    }
    this.dbURL = builder.toString();
  }

  @Override
  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(dbURL, setting.getUsername(), setting.getPassword());
  }

  @Override
  public Object getOriginal() {
    throw new UnsupportedOperationException("This client is from Java itself");
  }

  @Override
  public Setting getSetting() {
    return setting;
  }
}
