package me.hsgamer.hscore.map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * String Map but case-insensitive
 *
 * @param <V> the type of the value
 */
public class CaseInsensitiveStringMap<V> implements Map<String, V> {
  private final Map<String, V> original;

  /**
   * Create a new case-insensitive map
   *
   * @param original the original string map
   */
  public CaseInsensitiveStringMap(Map<String, V> original) {
    this.original = original;
  }

  private String getLowerCase(Object obj) {
    return String.valueOf(obj).toLowerCase(Locale.ROOT);
  }

  @Override
  public int size() {
    return this.original.size();
  }

  @Override
  public boolean isEmpty() {
    return this.original.isEmpty();
  }

  @Override
  public boolean containsKey(Object o) {
    return this.original.containsKey(getLowerCase(o));
  }

  @Override
  public boolean containsValue(Object o) {
    return this.original.containsValue(o);
  }

  @Override
  public V get(Object o) {
    return this.original.get(getLowerCase(o));
  }

  @Nullable
  @Override
  public V put(String s, V v) {
    return this.original.put(getLowerCase(s), v);
  }

  @Override
  public V remove(Object o) {
    return this.original.remove(getLowerCase(o));
  }

  @Override
  public void putAll(@NotNull Map<? extends String, ? extends V> map) {
    map.forEach(this::put);
  }

  @Override
  public void clear() {
    this.original.clear();
  }

  @NotNull
  @Override
  public Set<String> keySet() {
    return this.original.keySet();
  }

  @NotNull
  @Override
  public Collection<V> values() {
    return this.original.values();
  }

  @NotNull
  @Override
  public Set<Entry<String, V>> entrySet() {
    return this.original.entrySet();
  }
}
