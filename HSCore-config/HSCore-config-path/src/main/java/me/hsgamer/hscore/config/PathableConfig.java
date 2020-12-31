package me.hsgamer.hscore.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.FileConfiguration;

/**
 * A {@link Config} implementation to load {@link ConfigPath} automatically
 */
public class PathableConfig implements Config {
  private final Config config;

  /**
   * Create a pathable config
   *
   * @param config the original config
   */
  public PathableConfig(@NotNull final Config config) {
    this.config = config;
    setupConfig();
    PathLoader.loadPath(this);
    saveConfig();
  }

  @Override
  public void setupConfig() {
    config.setupConfig();
  }

  @Override
  public void reloadConfig() {
    config.reloadConfig();
  }

  @Override
  public void saveConfig() {
    config.saveConfig();
  }

  @Override
  public @NotNull FileConfiguration getConfig() {
    return config.getConfig();
  }

  @Override
  public @Nullable Object get(@NotNull String path, @Nullable Object def) {
    return config.get(path, def);
  }
}
