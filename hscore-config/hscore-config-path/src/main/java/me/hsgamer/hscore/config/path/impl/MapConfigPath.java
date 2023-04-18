package me.hsgamer.hscore.config.path.impl;

import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.config.path.SerializableMapConfigPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MapConfigPath extends SerializableMapConfigPath<Map<String, Object>> {
  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public MapConfigPath(PathString path, Map<String, Object> def) {
    super(path, def);
  }

  @Override
  public @Nullable Map<String, Object> convert(@NotNull Map<String, Object> rawValue) {
    return rawValue;
  }

  @Override
  public @Nullable Map<String, Object> convertToRaw(@NotNull Map<String, Object> value) {
    return value;
  }
}
