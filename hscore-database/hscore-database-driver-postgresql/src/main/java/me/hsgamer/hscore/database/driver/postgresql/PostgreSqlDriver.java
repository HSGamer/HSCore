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
    return "jdbc:postgresql://" + setting.getNormalizedHost() + '/' + setting.getDatabaseName() + Driver.createPropertyString(setting);
  }

  @Override
  public void applyDefaultSetting(Setting setting) {
    setting.setPort("5432").setUsername("postgres");
  }
}
