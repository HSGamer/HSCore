package me.hsgamer.hscore.config.simpleconfiguration;

import me.hsgamer.hscore.config.ConfigProvider;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.util.function.Function;

/**
 * The SimpleYAML config provider
 */
public class SimpleConfigProvider implements ConfigProvider<SimpleConfig> {
  private final Function<File, FileConfiguration> loader;

  /**
   * Create a new provider
   *
   * @param loader the loader
   */
  public SimpleConfigProvider(Function<File, FileConfiguration> loader) {
    this.loader = loader;
  }

  @Override
  public SimpleConfig loadConfiguration(File file) {
    return new SimpleConfig(file, loader);
  }
}
