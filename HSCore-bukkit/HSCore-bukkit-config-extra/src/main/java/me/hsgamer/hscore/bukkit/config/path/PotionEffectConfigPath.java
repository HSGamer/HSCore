package me.hsgamer.hscore.bukkit.config.path;

import java.util.Map;

import me.hsgamer.hscore.config.AdvancedConfigPath;
import me.hsgamer.hscore.config.Config;
import org.bukkit.potion.PotionEffect;

public class PotionEffectConfigPath extends AdvancedConfigPath<Map<String, Object>, PotionEffect> {

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
  public Map<String, Object> getFromConfig(Config config) {
    return config.getConfig().getConfigurationSection(path).getValues(false);
  }

  @Override
  public PotionEffect convert(Map<String, Object> rawValue) {
    return new PotionEffect(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(PotionEffect value) {
    return value.serialize();
  }
}
