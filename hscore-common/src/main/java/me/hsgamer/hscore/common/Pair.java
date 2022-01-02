package me.hsgamer.hscore.common;

import java.util.Map;

/**
 * Store the key and the value in pair
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public class Pair<K, V> {
  private final K key;
  private V value;

  private Pair(K key, V value) {
    this.key = key;
    this.value = value;
  }

  /**
   * Create a new pair
   *
   * @param key   the key
   * @param value the value
   * @param <K>   the type of the key
   * @param <V>   the type of the value
   *
   * @return the pair
   */
  public static <K, V> Pair<K, V> of(K key, V value) {
    return new Pair<>(key, value);
  }

  /**
   * Create a new pair from the map entry
   *
   * @param entry the map entry
   * @param <K>   the type of the key
   * @param <V>   the type of the value
   *
   * @return the pair
   */
  public static <K, V> Pair<K, V> of(Map.Entry<K, V> entry) {
    return new Pair<>(entry.getKey(), entry.getValue());
  }

  /**
   * Get the key
   *
   * @return the key
   */
  public K getKey() {
    return key;
  }

  /**
   * Get the value
   *
   * @return the value
   */
  public V getValue() {
    return value;
  }

  /**
   * Set the value
   *
   * @param value the value
   */
  public void setValue(V value) {
    this.value = value;
  }
}
