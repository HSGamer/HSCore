package me.hsgamer.hscore.map;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Linked String Map but case-insensitive
 */
public class CaseInsensitiveStringLinkedMap<V> extends LinkedHashMap<String, V> {

  public CaseInsensitiveStringLinkedMap() {
    super();
  }

  public CaseInsensitiveStringLinkedMap(Map<? extends String, ? extends V> map) {
    super();
    putAll(map);
  }

  @Override
  public V put(String key, V value) {
    return super.put(key.toLowerCase(Locale.ENGLISH), value);
  }

  @Override
  public V get(Object key) {
    return super.get(String.valueOf(key).toLowerCase(Locale.ENGLISH));
  }

  @Override
  public V getOrDefault(Object key, V def) {
    return super.getOrDefault(String.valueOf(key).toLowerCase(Locale.ENGLISH), def);
  }

  @Override
  public boolean containsKey(Object key) {
    return super.containsKey(String.valueOf(key).toLowerCase(Locale.ENGLISH));
  }

  @Override
  public V remove(Object key) {
    return super.remove(String.valueOf(key).toLowerCase(Locale.ENGLISH));
  }

  @Override
  public void putAll(Map<? extends String, ? extends V> m) {
    Map<String, V> mp = new LinkedHashMap<>();
    m.forEach((s, o) -> mp.put(s.toLowerCase(Locale.ENGLISH), o));
    super.putAll(mp);
  }
}
