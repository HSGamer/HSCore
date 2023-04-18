package me.hsgamer.hscore.bukkit.config.path;

import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.config.path.SerializableMapConfigPath;
import org.bukkit.block.banner.Pattern;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PatternConfigPath extends SerializableMapConfigPath<Pattern> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public PatternConfigPath(PathString path, Pattern def) {
    super(path, def);
  }

  @Override
  public Pattern convert(@NotNull final Map<String, Object> rawValue) {
    return new Pattern(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(@NotNull final Pattern value) {
    return value.serialize();
  }
}
