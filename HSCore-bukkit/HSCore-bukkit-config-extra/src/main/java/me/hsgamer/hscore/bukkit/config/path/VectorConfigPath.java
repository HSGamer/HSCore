package me.hsgamer.hscore.bukkit.config.path;

import java.util.Map;
import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.util.Vector;

public final class VectorConfigPath extends SerializableMapConfigPath<Vector> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  public VectorConfigPath(final String path, final Vector def) {
    super(path, def);
  }

  @Override
  public Vector convert(final Map<String, Object> rawValue) {
    return Vector.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(final Vector value) {
    return value.serialize();
  }

}
