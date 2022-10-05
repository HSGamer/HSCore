package me.hsgamer.hscore.config.configurate;

import me.hsgamer.hscore.config.ConfigProvider;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

import java.io.File;

/**
 * The Configurate config provider
 */
public class ConfigurateConfigProvider implements ConfigProvider<ConfigurateConfig> {
  private final AbstractConfigurationLoader.Builder<?, ?> builder;

  /**
   * Create a new config provider
   *
   * @param builder the config builder
   */
  public ConfigurateConfigProvider(AbstractConfigurationLoader.Builder<?, ?> builder) {
    this.builder = builder;
  }

  @Override
  public ConfigurateConfig loadConfiguration(File file) {
    return new ConfigurateConfig(file, builder);
  }
}
