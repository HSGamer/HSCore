package me.hsgamer.hscore.database.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.LocalDriver;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.Sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The SQL connection via HikariCP
 */
public class HikariSql implements Sql<HikariDataSource> {

  private final HikariDataSource hikariDataSource;

  /**
   * Create new SQL connection object
   *
   * @param setting the setting
   * @param driver  the driver
   */
  public HikariSql(Setting setting, Driver driver) {
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
    }
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
