package me.hsgamer.hscore.bukkit.config.path;

import java.util.Map;
import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.FireworkEffect;

public final class FireworkEffectConfigPath extends SerializableMapConfigPath<FireworkEffect> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  public FireworkEffectConfigPath(final String path, final FireworkEffect def) {
    super(path, def);
  }

  @Override
  public FireworkEffect convert(final Map<String, Object> rawValue) {
    return (FireworkEffect) FireworkEffect.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(final FireworkEffect value) {
    return value.serialize();
  }

}
