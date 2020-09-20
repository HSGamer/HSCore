package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.DefaultPath;

public final class LongDefaultPath extends DefaultPath<Long> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  public LongDefaultPath(final String path, final Long def) {
    super(path, def, o -> {
      try {
        return Long.parseLong(String.valueOf(o));
      } catch (final Exception e) {
        return def;
      }
    });
  }

}
