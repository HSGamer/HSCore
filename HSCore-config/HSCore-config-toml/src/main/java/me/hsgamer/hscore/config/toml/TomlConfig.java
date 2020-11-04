package me.hsgamer.hscore.config.toml;

import io.github.portlek.tomlgration.TomlConfiguration;
import me.hsgamer.hscore.config.BaseConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A TOML config file
 */
public class TomlConfig extends BaseConfig<TomlConfiguration> {

  /**
   * Create a TOML config
   *
   * @param file the config file
   */
  public TomlConfig(@NotNull final File file) {
    super(file, new TomlProvider());
  }

  /**
   * Create a TOML config
   *
   * @param dataFolder the data folder of the file
   * @param filename   the file name
   */
  public TomlConfig(@NotNull final File dataFolder, @NotNull final String filename) {
    this(new File(dataFolder, filename));
  }
}
