package me.hsgamer.hscore.bukkit.key;

import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.Map;

/**
 * The abstract key manager
 */
public abstract class AbstractKeyManager implements KeyManager {
  private final Map<String, NamespacedKey> namespacedKeyMap = new HashMap<>();

  @Override
  public NamespacedKey createKey(String key) {
    return namespacedKeyMap.computeIfAbsent(key, this::newKey);
  }

  /**
   * Create a new key
   *
   * @param key the key
   *
   * @return the new key
   */
  public abstract NamespacedKey newKey(String key);
}
