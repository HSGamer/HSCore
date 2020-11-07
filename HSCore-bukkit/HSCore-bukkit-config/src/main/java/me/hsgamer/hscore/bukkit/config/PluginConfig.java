package me.hsgamer.hscore.bukkit.config;

import me.hsgamer.hscore.config.BaseConfig;
import me.hsgamer.simplebukkityaml.configuration.file.YamlFile;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A {@link me.hsgamer.hscore.config.Config} for Bukkit
 */
public class PluginConfig extends BaseConfig<YamlFile> {
  /**
   * Create a config
   *
   * @param plugin   the plugin
   * @param fileName the file name
   */
  public PluginConfig(Plugin plugin, String fileName) {
    this(new File(plugin.getDataFolder(), fileName));
  }

  /**
   * Create a config with a provider
   *
   * @param file the config file
   */
  public PluginConfig(@NotNull File file) {
    super(file, new PluginYamlProvider());
  }
}
