package me.hsgamer.hscore.database.client.sql.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.LocalDriver;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.client.sql.SqlClient;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The SQL client with HikariCP
 */
public class HikariSqlClient implements SqlClient<HikariDataSource> {

  private final Setting setting;
  private final HikariDataSource hikariDataSource;

  /**
   * Create new SQL client
   *
   * @param setting the setting
   * @param driver  the driver
   */
  public HikariSqlClient(Setting setting, Driver driver) {
    this.setting = setting;
    final HikariConfig config = new HikariConfig();
    config.setDriverClassName(driver.getClassName());
    config.setJdbcUrl(driver.convertURL(setting));
    if (driver instanceof LocalDriver) {
      config.setPoolName("LOCALDB-" + setting.getDatabaseName());
      config.setMaxLifetime(60000L);
      config.setIdleTimeout(45000L);
      config.setMaximumPoolSize(50);
    } else {
      config.setUsername(setting.getUsername());
      config.setPassword(setting.getPassword());
      setting.getProperties().forEach(config::addDataSourceProperty);
      config.addDataSourceProperty("cachePrepStmts", true);
      config.addDataSourceProperty("prepStmtCacheSize", 250);
      config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
      config.addDataSourceProperty("useServerPrepStmts", true);
      config.addDataSourceProperty("useLocalSessionState", true);
      config.addDataSourceProperty("rewriteBatchedStatements", true);
      config.addDataSourceProperty("cacheResultSetMetadata", true);
      config.addDataSourceProperty("cacheServerConfiguration", true);
      config.addDataSourceProperty("elideSetAutoCommits", true);
      config.addDataSourceProperty("maintainTimeStats", false);
    }
    this.hikariDataSource = new HikariDataSource(config);
  }

  @Override
  public HikariDataSource getOriginal() {
    return hikariDataSource;
  }

  @Override
  public Setting getSetting() {
    return setting;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return hikariDataSource.getConnection();
  }
}
