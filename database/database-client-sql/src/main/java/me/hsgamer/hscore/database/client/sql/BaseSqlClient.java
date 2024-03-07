package me.hsgamer.hscore.database.client.sql;

import me.hsgamer.hscore.database.BaseClient;
import me.hsgamer.hscore.database.Setting;

/**
 * An abstraction of {@link SqlClient}
 *
 * @param <T> the original
 */
public abstract class BaseSqlClient<T> extends BaseClient<T> implements SqlClient<T> {
  /**
   * Create a new client
   *
   * @param setting the setting
   */
  protected BaseSqlClient(Setting setting) {
    super(setting);
  }
}
