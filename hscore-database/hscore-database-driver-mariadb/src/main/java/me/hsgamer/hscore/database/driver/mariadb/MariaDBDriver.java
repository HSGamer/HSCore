package me.hsgamer.hscore.database.driver.mariadb;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;

/**
 * A driver for MariaDB
 */
public class MariaDBDriver implements Driver {
  @Override
  public Class<? extends java.sql.Driver> getDriverClass() {
    return org.mariadb.jdbc.Driver.class;
  }

  @Override
  public String convertURL(Setting setting) {
    return "jdbc:mariadb://" + setting.getNormalizedHost() + '/' + setting.getDatabaseName() + Driver.createPropertyString(setting);
  }

  @Override
  public Setting applyDefaultSetting(Setting setting) {
    return setting.setPort("3306").setUsername("root");
  }
}
