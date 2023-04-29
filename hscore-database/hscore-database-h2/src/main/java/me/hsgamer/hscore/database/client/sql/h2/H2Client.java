package me.hsgamer.hscore.database.client.sql.h2;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.client.sql.BaseSqlClient;
import me.hsgamer.hscore.database.driver.h2.H2BaseDriver;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class H2Client extends BaseSqlClient<JdbcDataSource> {
  private final JdbcDataSource dataSource;

  /**
   * Create a new H2 client
   *
   * @param setting the setting
   */
  public H2Client(Setting setting) {
    super(setting);
    Driver driver = setting.getDriver();
    if (!(driver instanceof H2BaseDriver)) {
      throw new IllegalArgumentException("The driver must be H2");
    }
    dataSource = new JdbcDataSource();
    dataSource.setURL(driver.convertURL(setting));
    dataSource.setUser(setting.getUsername());
    dataSource.setPassword(setting.getPassword());
  }

  @Override
  public JdbcDataSource getOriginal() {
    return dataSource;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}
