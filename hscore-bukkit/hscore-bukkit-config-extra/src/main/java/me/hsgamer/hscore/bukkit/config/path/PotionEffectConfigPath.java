package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.config.path.SerializableMapConfigPath;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PotionEffectConfigPath extends SerializableMapConfigPath<PotionEffect> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public PotionEffectConfigPath(String path, PotionEffect def) {
    super(path, def);
  }

  @Override
  public PotionEffect convert(@NotNull final Map<String, Object> rawValue) {
    return new PotionEffect(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(@NotNull final PotionEffect value) {
    return value.serialize();
  }
}
