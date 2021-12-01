package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.bukkit.config.object.PlayableSound;
import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SoundConfigPath extends SerializableMapConfigPath<PlayableSound> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public SoundConfigPath(String path, PlayableSound def) {
    super(path, def);
  }

  @Override
  public PlayableSound convert(@NotNull final Map<String, Object> rawValue) {
    return PlayableSound.deserialize(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(@NotNull final PlayableSound value) {
    return value.serialize();
  }
}
