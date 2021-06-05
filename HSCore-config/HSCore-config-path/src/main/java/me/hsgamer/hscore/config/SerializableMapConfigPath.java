package me.hsgamer.hscore.config;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * An serializable map config path
 *
 * @param <T> the type of the final value
 */
public abstract class SerializableMapConfigPath<T> extends AdvancedConfigPath<Map<String, Object>, T> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public SerializableMapConfigPath(String path, T def) {
    super(path, def);
  }

  @Override
  public final Map<String, Object> getFromConfig(@NotNull final Config config) {
    final Object mapObj = config.get(getPath());
    if (mapObj instanceof Map<?, ?>) {
      //noinspection unchecked
      return ((Map<String, Object>) mapObj);
    }
    return config.getNormalizedValues(getPath(), false);
  }
}
