package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.DefaultPath;

public final class IntegerDefaultPath extends DefaultPath<Integer> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  public IntegerDefaultPath(final String path, final Integer def) {
    super(path, def, o -> {
      try {
        return Integer.parseInt(String.valueOf(o));
      } catch (final Exception e) {
        return def;
      }
    });
  }

}
