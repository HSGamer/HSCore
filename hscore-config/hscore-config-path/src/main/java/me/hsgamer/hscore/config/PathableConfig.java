package me.hsgamer.hscore.config;

/**
 * A {@link Config} implementation to load {@link ConfigPath} automatically
 */
public class PathableConfig extends DecorativeConfig {

  /**
   * Create a pathable config
   *
   * @param config the original config
   */
  public PathableConfig(Config config) {
    super(config);
  }

  @Override
  public void setup() {
    super.setup();
    PathLoader.loadPath(this);
    this.save();
  }

  @Override
  public void reload() {
    super.reload();
    PathLoader.reloadPath(this);
  }
}
