package me.hsgamer.hscore.database.driver.h2;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;

import java.util.Map;

/**
 * A driver for H2
 */
public interface H2BaseDriver extends Driver {
  @Override
  default Class<? extends java.sql.Driver> getDriverClass() {
    return org.h2.Driver.class;
  }

  @Override
  default String convertURL(Setting setting) {
    StringBuilder builder = new StringBuilder();
    builder.append("jdbc:h2:");
    builder.append(getConnectionString(setting));
    Map<String, Object> properties = setting.getDriverProperties();
    if (!properties.isEmpty()) {
      builder.append(";");
      properties.forEach((key, value) -> {
        builder.append(key);
        builder.append("=");
        builder.append(value);
        builder.append(";");
      });
    }
    return builder.toString();
  }

  String getConnectionString(Setting setting);
}
