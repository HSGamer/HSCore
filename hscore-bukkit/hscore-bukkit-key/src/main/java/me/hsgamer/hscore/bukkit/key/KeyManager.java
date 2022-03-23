package me.hsgamer.hscore.bukkit.key;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

/**
 * An interface for all {@link NamespacedKey} manager
 */
public interface KeyManager {
  /**
   * Create a new {@link NamespacedKey}.
   * This should have a function to store the created {@link NamespacedKey} for later uses.
   *
   * @param key the key
   *
   * @return the {@link NamespacedKey}
   */
  NamespacedKey createKey(String key);

  /**
   * Create a new key pair
   *
   * @param key          the key
   * @param dataType     the data type for the value
   * @param defaultValue the default value if not found
   * @param <T>          the primary object type that is stored in the given key
   * @param <Z>          the retrieved object type when applying this data type
   *
   * @return the new key pair
   */
  default <T, Z> PluginKeyPair<T, Z> createKeyPair(String key, PersistentDataType<T, Z> dataType, Z defaultValue) {
    return new PluginKeyPair<>(createKey(key), dataType, defaultValue);
  }

  /**
   * Create a new key pair with null default value
   *
   * @param key      the key
   * @param dataType the data type for the value
   * @param <T>      the primary object type that is stored in the given key
   * @param <Z>      the retrieved object type when applying this data type
   *
   * @return the new key pair
   */
  default <T, Z> PluginKeyPair<T, Z> createKeyPair(String key, PersistentDataType<T, Z> dataType) {
    return createKeyPair(key, dataType, null);
  }
}
