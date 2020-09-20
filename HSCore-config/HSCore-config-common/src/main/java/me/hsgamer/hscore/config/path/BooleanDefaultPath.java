package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.DefaultPath;

public final class BooleanDefaultPath extends DefaultPath<Boolean> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  public BooleanDefaultPath(final String path, final Boolean def) {
    super(path, def, o -> Boolean.parseBoolean(String.valueOf(o)));
  }

}
