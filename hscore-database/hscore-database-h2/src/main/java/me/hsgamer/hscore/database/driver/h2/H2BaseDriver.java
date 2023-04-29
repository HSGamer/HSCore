package me.hsgamer.hscore.database.driver.h2;

import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.client.sql.h2.H2Client;

import java.util.function.Consumer;

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
    return "jdbc:h2:" +
      getConnectionString(setting) +
      Driver.createPropertyString(setting, ";", ";");
  }

  String getConnectionString(Setting setting);

  /**
   * Create a new {@link H2Client}
   *
   * @param settingConsumer the setting consumer
   *
   * @return the new client
   */
  default H2Client createClient(Consumer<Setting> settingConsumer) {
    Setting setting = Setting.create(this);
    settingConsumer.accept(setting);
    return new H2Client(setting);
  }
}
