package me.hsgamer.hscore.sql.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.hsgamer.hscore.sql.Driver;
import me.hsgamer.hscore.sql.Setting;
import me.hsgamer.hscore.sql.Sql;
import me.hsgamer.hscore.sql.driver.MySqlDriver;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The MySQL connection via HikariCP
 */
public class HikariMySql implements Sql<HikariDataSource> {

  private final HikariDataSource hikariDataSource;

  /**
   * Create new SQL connection object
   *
   * @param setting the setting
   */
  public HikariMySql(Setting setting) {
    this(setting, new MySqlDriver());
  }

  /**
   * Create new SQL connection object
   *
   * @param setting the setting
   * @param driver  the driver
   */
  public HikariMySql(Setting setting, Driver driver) {
    final HikariConfig config = new HikariConfig();
    config.setJdbcUrl(driver.convertURL(setting));
    config.setUsername(setting.getUsername());
    config.setPassword(setting.getPassword());
    config.setDriverClassName(driver.getName());
    config.addDataSourceProperty("useSSL", String.valueOf(setting.isUseSSL()));
    config.addDataSourceProperty("verifyServerCertificate", String.valueOf(setting.isCertVerify()));
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
