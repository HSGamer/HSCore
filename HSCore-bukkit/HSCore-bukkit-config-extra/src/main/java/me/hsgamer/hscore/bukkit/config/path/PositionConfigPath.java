package me.hsgamer.hscore.bukkit.config.path;

import java.util.Map;
import me.hsgamer.hscore.bukkit.config.object.Position;
import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.jetbrains.annotations.NotNull;

public final class PositionConfigPath extends SerializableMapConfigPath<Position> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public PositionConfigPath(String path, Position def) {
    super(path, def);
  }

  @Override
  public Position convert(@NotNull final Map<String, Object> rawValue) {
    return Position.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(@NotNull final Position value) {
    return value.serialize();
  }
}
