package me.hsgamer.hscore.bukkit.addon.object;

import me.hsgamer.hscore.bukkit.addon.exception.RequiredAddonPathException;
import me.hsgamer.hscore.bukkit.addon.object.path.AddonPath;
import org.bukkit.configuration.file.YamlConfiguration;

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

  /**
   * Get the value from addon.yml
   *
   * @param <T>       the type of value
   * @param addonPath the path to the value
   * @return the value
   * @throws RequiredAddonPathException if the path is required but is not found in addon.yml
   */
  public <T> T get(AddonPath<T> addonPath) {
    if (addonPath.isRequired() && !configuration.isSet(addonPath.getPath())) {
      throw new RequiredAddonPathException(
          addonPath.getPath() + " is not found in the addon '" + name + "'");
    }

    Object value = configuration.get(addonPath.getPath());
    return value == null ? null : addonPath.convertType(value);
  }
}
