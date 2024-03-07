package me.hsgamer.hscore.database;

/**
 * An abstraction of {@link Client} with {@link Setting}
 *
 * @param <T> the original
 */
public abstract class BaseClient<T> implements Client<T> {
  /**
   * The setting
   */
  protected final Setting setting;

  /**
   * Create a new client
   *
   * @param setting the setting
   */
  protected BaseClient(Setting setting) {
    this.setting = setting;
  }

  @Override
  public Setting getSetting() {
    return setting;
  }
}
