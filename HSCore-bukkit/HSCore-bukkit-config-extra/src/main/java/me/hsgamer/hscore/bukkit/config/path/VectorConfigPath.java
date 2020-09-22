package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.util.Vector;

import java.util.Map;

public final class VectorConfigPath extends SerializableMapConfigPath<Vector> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public VectorConfigPath(String path, Vector def) {
    super(path, def);
  }

  @Override
  public Vector convert(Map<String, Object> rawValue) {
    return Vector.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(Vector value) {
    return value.serialize();
  }
}
