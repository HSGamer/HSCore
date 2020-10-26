package me.hsgamer.hscore.config;

import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;

/**
 * A {@link BaseConfig} implementation to load {@link ConfigPath} automatically
 *
 * @param <F> File configuration class type
 */
public class PathableConfig<F extends FileConfiguration> extends BaseConfig<F> {

  /**
   * Create a config with a provider
   *
   * @param file     the config file
   * @param provider the provider
   */
  public PathableConfig(@NotNull final File file, @NotNull final ConfigProvider<F> provider) {
    super(file, provider);
    PathLoader.loadPath(this);
  }
}
