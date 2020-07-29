package me.hsgamer.hscore.bukkit.addon;

// TODO: Add List of AddonSetting
public final class AddonDescription {

  private final String name;
  private final String version;
  private final String mainClass;

  public AddonDescription(String name, String version, String mainClass) {
    this.name = name;
    this.version = version;
    this.mainClass = mainClass;
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
}
