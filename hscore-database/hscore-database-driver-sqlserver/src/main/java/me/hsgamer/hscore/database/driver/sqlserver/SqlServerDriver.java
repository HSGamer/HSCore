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
    if (!setting.getDatabaseName().isEmpty()) {
      builder.append(";databaseName=");
      builder.append(setting.getDatabaseName());
    }
    for (String key : setting.getClientPropertyStrings()) {
      builder.append(";");
      builder.append(key);
    }
    return builder.toString();
  }

  @Override
  public Setting applyDefaultSetting(Setting setting) {
    return setting.setPort("1433").setUsername("sa");
  }
}
