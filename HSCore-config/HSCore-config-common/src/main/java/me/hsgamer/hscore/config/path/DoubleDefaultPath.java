package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.DefaultPath;

public final class DoubleDefaultPath extends DefaultPath<Double> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  public DoubleDefaultPath(final String path, final Double def) {
    super(path, def, o -> {
      try {
        return Double.parseDouble(String.valueOf(o));
      } catch (final Exception e) {
        return def;
      }
    });
  }

}
