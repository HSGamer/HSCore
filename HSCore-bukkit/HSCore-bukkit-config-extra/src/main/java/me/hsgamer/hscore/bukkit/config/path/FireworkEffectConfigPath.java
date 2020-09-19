package me.hsgamer.hscore.bukkit.config.path;

import java.util.Map;
import me.hsgamer.hscore.bukkit.config.AdvancedConfigPath;
import me.hsgamer.hscore.bukkit.config.PluginConfig;
import org.bukkit.FireworkEffect;

public class FireworkEffectConfigPath extends
    AdvancedConfigPath<Map<String, Object>, FireworkEffect> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public FireworkEffectConfigPath(String path, FireworkEffect def) {
    super(path, def);
  }

  @Override
  public Map<String, Object> getFromConfig(PluginConfig pluginConfig) {
    return pluginConfig.getConfig().getConfigurationSection(path).getValues(false);
  }

  @Override
  public FireworkEffect convert(Map<String, Object> rawValue) {
    return ((FireworkEffect) FireworkEffect.deserialize(rawValue));
  }

  @Override
  public Map<String, Object> convertToRaw(FireworkEffect value) {
    return value.serialize();
  }
}
