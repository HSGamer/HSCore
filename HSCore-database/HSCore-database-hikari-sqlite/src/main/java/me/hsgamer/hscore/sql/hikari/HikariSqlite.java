package me.hsgamer.hscore.sql.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.hsgamer.hscore.sql.LocalDriver;
import me.hsgamer.hscore.sql.Setting;
import me.hsgamer.hscore.sql.Sql;
import me.hsgamer.hscore.sql.driver.SqliteDriver;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * The SQLite connection via HikariCP
 */
public class HikariSqlite implements Sql<HikariDataSource> {
  private final HikariDataSource hikariDataSource;

  /**
   * Create new SQL connection object
   *
   * @param setting the setting
   */
  public HikariSqlite(Setting setting) {
    this(setting, new SqliteDriver(new File(".")));
  }

  /**
   * Create new SQL connection object
   *
   * @param setting     the setting
   * @param localDriver the local driver
   */
  public HikariSqlite(Setting setting, LocalDriver localDriver) {
    final HikariConfig config = new HikariConfig();
    config.setPoolName("LOCALDB-" + setting.getDatabaseName());
    config.setDriverClassName(localDriver.getName());
    config.setJdbcUrl(localDriver.convertURL(setting));
    config.setMaxLifetime(60000L);
    config.setIdleTimeout(45000L);
    config.setMaximumPoolSize(50);
    this.hikariDataSource = new HikariDataSource(config);
  }

  @Override
  public HikariDataSource getOriginal() {
    return hikariDataSource;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return hikariDataSource.getConnection();
  }
}
