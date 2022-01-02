package me.hsgamer.hscore.database;

/**
 * The client
 *
 * @param <T> the original
 */
public interface Client<T> {

  /**
   * Get the original
   *
   * @return the original
   */
  T getOriginal();

  /**
   * Get the setting
   *
   * @return the setting
   */
  Setting getSetting();
}
