package me.hsgamer.hscore.database.driver.sqlserver;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;

/**
 * A driver for Microsoft SQL Server
 */
public class SqlServerDriver implements Driver {
  @Override
  public Class<? extends java.sql.Driver> getDriverClass() {
    return SQLServerDriver.class;
  }

  @Override
  public String convertURL(Setting setting) {
    StringBuilder builder = new StringBuilder();
    builder.append("jdbc:sqlserver://");
    builder.append(setting.getNormalizedHost());
    builder.append(Driver.createPropertyString(setting, ";", ";"));
    if (!setting.getDatabaseName().isEmpty()) {
      builder.append(";databaseName=").append(setting.getDatabaseName());
    }
    return builder.toString();
  }

  @Override
  public void applyDefaultSetting(Setting setting) {
    setting.setPort("1433").setUsername("sa");
  }
}
