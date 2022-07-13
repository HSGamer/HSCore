package me.hsgamer.hscore.database.driver.postgresql;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;

/**
 * A driver for PostgreSQL
 */
public class PostgreSqlDriver implements Driver {
  @Override
  public Class<? extends java.sql.Driver> getDriverClass() {
    return org.postgresql.Driver.class;
  }

  @Override
  public String convertURL(Setting setting) {
    return "jdbc:postgresql://" + setting.getHost() + ':' + setting.getPort() + '/' + setting.getDatabaseName() + Driver.createPropertyString(setting);
  }

  @Override
  public Setting applyDefaultSetting(Setting setting) {
    return setting.setPort("5432").setUsername("postgres");
  }
}
