package me.hsgamer.hscore.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple config file
 *
 * @param <F> File configuration class type
 */
public class BaseConfig<F extends FileConfiguration> implements Config {

  /**
   * The logger for ease
   */
  public static final Logger LOGGER = Logger.getLogger("Config");

  private final File file;
  private final ConfigProvider<F> provider;

  private F fileConfiguration;

  /**
   * Create a config with a provider
   *
   * @param file     the config file
   * @param provider the provider
   */
  public BaseConfig(@NotNull final File file, @NotNull final ConfigProvider<F> provider) {
    this.file = file;
    this.provider = provider;
    setupConfig();
  }

  @Override
  public final void setupConfig() {
    if (!file.exists()) {
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }

      try {
        file.createNewFile();
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, e, () -> "Something wrong when creating " + getFileName());
      }
    }
    fileConfiguration = provider.loadConfiguration(file);
  }

  @Override
  public final void reloadConfig() {
    fileConfiguration = provider.loadConfiguration(file);
  }

  @Override
  public final void saveConfig() {
    try {
      provider.saveConfiguration(fileConfiguration, file);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when saving " + getFileName());
    }
  }

  @NotNull
  @Override
  public final F getConfig() {
    if (fileConfiguration == null) {
      setupConfig();
    }
    return fileConfiguration;
  }

  @Override
  @Nullable
  public final Object get(@NotNull final String path, @Nullable final Object def) {
    return getConfig().get(path, def);
  }

  /**
   * Get the file name
   *
   * @return the file name
   */
  @NotNull
  public final String getFileName() {
    return file.getName();
  }
}