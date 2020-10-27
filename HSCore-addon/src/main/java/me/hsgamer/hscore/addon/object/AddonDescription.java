package me.hsgamer.hscore.addon.object;

import me.hsgamer.hscore.addon.exception.RequiredAddonPathException;
import me.hsgamer.hscore.config.ConfigProvider;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
  private final FileConfiguration configuration;

  public AddonDescription(@NotNull String name, @NotNull String version, @NotNull String mainClass,
                          @NotNull FileConfiguration configuration) {
    this.name = name;
    this.version = version;
    this.mainClass = mainClass;
    this.configuration = configuration;
  }

  /**
   * Generate the addon description
   *
   * @param jar                 the addon jar
   * @param addonConfigFileName the name of the addon config file
   * @param provider            the config provider
   * @return the addon description
   * @throws IOException if there is an error when loading the addon jar
   */
  @NotNull
  public static <T extends FileConfiguration> AddonDescription get(@NotNull final JarFile jar, @NotNull final String addonConfigFileName, @NotNull final ConfigProvider<T> provider) throws IOException {
    // Load addon.yml file
    JarEntry entry = jar.getJarEntry(addonConfigFileName);
    if (entry == null) {
      throw new NoSuchFileException("Addon '" + jar.getName() + "' doesn't contain " + addonConfigFileName + " file");
    }
    Reader reader = new InputStreamReader(jar.getInputStream(entry));
    FileConfiguration data = provider.loadConfiguration(reader);

    // Load required descriptions
    String name = data.getString("name");
    String version = data.getString("version");
    String mainClass = data.getString("main");
    if (name == null) {
      throw new RequiredAddonPathException("Addon '" + jar.getName() + "' doesn't have a name on " + addonConfigFileName);
    }
    if (version == null) {
      throw new RequiredAddonPathException("Addon '" + jar.getName() + "' doesn't have a version on " + addonConfigFileName);
    }
    if (mainClass == null) {
      throw new RequiredAddonPathException("Addon '" + jar.getName() + "' doesn't have a main class on " + addonConfigFileName);
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
   * Get the addon config file
   *
   * @return the file
   */
  @NotNull
  public final FileConfiguration getConfiguration() {
    return configuration;
  }
}
