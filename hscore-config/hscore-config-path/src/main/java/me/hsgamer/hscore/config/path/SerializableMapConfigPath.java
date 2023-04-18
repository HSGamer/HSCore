package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  public SerializableMapConfigPath(PathString path, T def) {
    super(path, def);
  }

  @Override
  @Nullable
  public final Map<String, Object> getFromConfig(@NotNull final Config config) {
    if (!config.contains(getPath())) return null;
    final Object mapObj = config.getNormalized(getPath());
    if (mapObj instanceof Map<?, ?>) {
      //noinspection unchecked
      return (Map<String, Object>) mapObj;
    }
    return PathString.toPathMap(".", config.getNormalizedValues(getPath(), false));
  }
}
