package me.hsgamer.hscore.bukkit.addon.object;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * The description for the Addon
 */
public final class AddonDescription {

  private final String name;
  private final String version;
  private final String mainClass;
  private final YamlConfiguration configuration;

  public AddonDescription(String name, String version, String mainClass,
      YamlConfiguration configuration) {
    this.name = name;
    this.version = version;
    this.mainClass = mainClass;
    this.configuration = configuration;
  }

  /**
   * Get the name of the addon
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Get the version of the addon
   *
   * @return the version
   */
  public String getVersion() {
    return version;
  }

  /**
   * Get the main class of the addon
   *
   * @return the path to the main class
   */
  public String getMainClass() {
    return mainClass;
  }

  /**
   * Get the addon.yml file
   *
   * @return the file
   */
  public YamlConfiguration getConfiguration() {
    return configuration;
  }
}
