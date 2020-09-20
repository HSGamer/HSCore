package me.hsgamer.hscore.bukkit.config.path;

import java.util.Map;
import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.potion.PotionEffect;

public final class PotionEffectConfigPath extends SerializableMapConfigPath<PotionEffect> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  public PotionEffectConfigPath(final String path, final PotionEffect def) {
    super(path, def);
  }

  @Override
  public PotionEffect convert(final Map<String, Object> rawValue) {
    return new PotionEffect(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(final PotionEffect value) {
    return value.serialize();
  }

}
