package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.DefaultPath;

public final class StringDefaultPath extends DefaultPath<String> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  public StringDefaultPath(final String path, final String def) {
    super(path, def, String::valueOf);
  }

}
