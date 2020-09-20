package me.hsgamer.hscore.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import org.simpleyaml.configuration.file.FileConfiguration;

/**
 * A simple config file
 */
public abstract class ConfigEnvelope implements Config {

  private final File file;

  private final FileConfigProvider provider;

  private FileConfiguration fileConfiguration;

  /**
   * Create a config with a provider
   *
   * @param file the config file
   * @param provider the provider
   */
  protected ConfigEnvelope(final File file, final FileConfigProvider provider) {
    this.file = file;
    this.provider = provider;
    this.setUpConfig();
  }

  /**
   * Reload the config
   */
  @Override
  public final void reloadConfig() {
    this.fileConfiguration = this.provider.loadConfiguration(this.file);
  }

  /**
   * Save the config
   */
  @Override
  public final void saveConfig() {
    try {
      this.provider.saveConfiguration(this.fileConfiguration, this.file);
    } catch (final IOException e) {
      Config.LOGGER.log(Level.WARNING, e, () -> "Something wrong when saving " + this.getFileName());
    }
  }

  /**
   * Get the instance of the config file
   *
   * @return the config
   */
  @Override
  public final FileConfiguration getConfig() {
    if (this.fileConfiguration == null) {
      this.setUpConfig();
    }
    return this.fileConfiguration;
  }

  /**
   * Get the file name
   *
   * @return the file name
   */
  @Override
  public final String getFileName() {
    return this.file.getName();
  }

  /**
   * Set up the config
   */
  private void setUpConfig() {
    if (!this.file.exists()) {
      if (!this.file.getParentFile().exists()) {
        this.file.getParentFile().mkdirs();
      }

      try {
        this.file.createNewFile();
      } catch (final IOException e) {
        Config.LOGGER.log(Level.WARNING, e, () -> "Something wrong when creating " + this.getFileName());
      }
    }
    this.fileConfiguration = this.provider.loadConfiguration(this.file);
    Arrays.stream(this.getClass().getDeclaredFields())
      .filter(field -> BaseConfigPath.class.isAssignableFrom(field.getType()))
      .forEach(field -> {
        try {
          ((ConfigPath<?>) field.get(this)).setConfig(this);
        } catch (final IllegalAccessException e) {
          e.printStackTrace();
        }
      });
  }

}