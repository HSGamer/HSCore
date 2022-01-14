package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.config.path.SerializableMapConfigPath;
import org.bukkit.FireworkEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FireworkEffectConfigPath extends SerializableMapConfigPath<FireworkEffect> {

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
  public FireworkEffect convert(@NotNull final Map<String, Object> rawValue) {
    return (FireworkEffect) FireworkEffect.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(@NotNull final FireworkEffect value) {
    return value.serialize();
  }
}
