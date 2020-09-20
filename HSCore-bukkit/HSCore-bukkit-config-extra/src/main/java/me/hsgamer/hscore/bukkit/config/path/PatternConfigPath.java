package me.hsgamer.hscore.bukkit.config.path;

import java.util.Map;
import me.hsgamer.hscore.config.SerializableMapConfigPath;
import org.bukkit.block.banner.Pattern;

public final class PatternConfigPath extends SerializableMapConfigPath<Pattern> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  public PatternConfigPath(final String path, final Pattern def) {
    super(path, def);
  }

  @Override
  public Pattern convert(final Map<String, Object> rawValue) {
    return new Pattern(rawValue);
  }

  @Override
  public Map<String, Object> convertToRaw(final Pattern value) {
    return value.serialize();
  }

}
