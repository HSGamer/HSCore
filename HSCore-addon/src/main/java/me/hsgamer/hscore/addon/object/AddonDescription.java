package me.hsgamer.hscore.addon.object;

import me.hsgamer.hscore.addon.exception.RequiredAddonPathException;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.NoSuchFileException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The description for the Addon
 */
public final class AddonDescription {

  private final String name;
  private final String version;
  private final String mainClass;
  private final YamlConfiguration configuration;

  public AddonDescription(@NotNull String name, @NotNull String version, @NotNull String mainClass,
                          @NotNull YamlConfiguration configuration) {
    this.name = name;
    this.version = version;
    this.mainClass = mainClass;
    this.configuration = configuration;
  }

  /**
   * Generate the addon description
   *
   * @param jar the addon jar
   * @return the addon description
   * @throws IOException                   if there is an error when loading the addon jar
   * @throws InvalidConfigurationException if the addon.yml file is invalid
   */
  @NotNull
  public static AddonDescription get(@NotNull final JarFile jar) throws IOException, InvalidConfigurationException {
    // Load addon.yml file
    JarEntry entry = jar.getJarEntry("addon.yml");
    if (entry == null) {
      throw new NoSuchFileException(
        "Addon '" + jar.getName() + "' doesn't contain addon.yml file");
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(jar.getInputStream(entry)));
    YamlConfiguration data = new YamlConfiguration();
    data.load(reader);

    // Load required descriptions
    String name = data.getString("name");
    String version = data.getString("version");
    String mainClass = data.getString("main");
    if (name == null) {
      throw new RequiredAddonPathException("Addon '" + jar.getName() + "' doesn't have a name on addon.yml");
    }
    if (version == null) {
      throw new RequiredAddonPathException("Addon '" + jar.getName() + "' doesn't have a version on addon.yml");
    }
    if (mainClass == null) {
      throw new RequiredAddonPathException("Addon '" + jar.getName() + "' doesn't have a main class on addon.yml");
    }
    return new AddonDescription(name, version, mainClass, data);
  }

  /**
   * Get the name of the addon
   *
   * @return the name
   */
  @NotNull
  public final String getName() {
    return name;
  }

  /**
   * Get the version of the addon
   *
   * @return the version
   */
  @NotNull
  public final String getVersion() {
    return version;
  }

  /**
   * Get the main class of the addon
   *
   * @return the path to the main class
   */
  @NotNull
  public final String getMainClass() {
    return mainClass;
  }

  /**
   * Get the addon.yml file
   *
   * @return the file
   */
  @NotNull
  public final YamlConfiguration getConfiguration() {
    return configuration;
  }
}
