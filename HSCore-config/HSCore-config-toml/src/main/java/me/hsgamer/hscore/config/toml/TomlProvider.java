package me.hsgamer.hscore.config.toml;

import io.github.portlek.tomlgration.TomlConfiguration;
import me.hsgamer.hscore.config.ConfigProvider;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * a TOML implementation for {@link ConfigProvider}.
 */
public class TomlProvider implements ConfigProvider<TomlConfiguration> {

  @NotNull
  @Override
  public TomlConfiguration loadConfiguration(File file) {
    return TomlConfiguration.loadConfiguration(file);
  }
}
