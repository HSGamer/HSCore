package me.hsgamer.hscore.addon.object;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.NoSuchFileException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import me.hsgamer.hscore.addon.exception.RequiredAddonPathException;
import me.hsgamer.hscore.config.ConfigProvider;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;

/**
 * The description for the Addon
 */
public final class AddonDescription {

  /**
   * The name of the addon
   */
  @NotNull
  private final String name;

  /**
   * The version of the addon
   */
  @NotNull
  private final String version;

  /**
   * The main class of the addon
   */
  @NotNull
  private final String mainClass;

  /**
   * The configuration of the addon
   */
  @NotNull
  private final FileConfiguration configuration;

  /**
   * Create an addon description
   *
   * @param name          the name of the addon
   * @param version       the version of the addon
   * @param mainClass     the main class of the addon
   * @param configuration the configuration of the addon
   */
  public AddonDescription(@NotNull final String name, @NotNull final String version, @NotNull final String mainClass,
                          @NotNull final FileConfiguration configuration) {
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
   *
   * @return the addon description
   *
   * @throws IOException if there is an error when loading the addon jar
   */
  @NotNull
  public static AddonDescription get(@NotNull final JarFile jar, @NotNull final String addonConfigFileName,
                                     @NotNull final ConfigProvider<? extends FileConfiguration> provider)
    throws IOException {
    // Load the addon config file
    final JarEntry entry = jar.getJarEntry(addonConfigFileName);
    if (entry == null) {
      throw new NoSuchFileException("Addon '" + jar.getName() + "' doesn't contain " + addonConfigFileName + " file");
    }
    final Reader reader = new InputStreamReader(jar.getInputStream(entry));
    final FileConfiguration data = provider.loadConfiguration(reader);
    // Load required descriptions
    final String name = data.getString("name");
    final String version = data.getString("version");
    final String mainClass = data.getString("main");
    if (name == null) {
      throw new RequiredAddonPathException("Addon '" + jar.getName() + "' doesn't have a name on " +
        addonConfigFileName);
    }
    if (version == null) {
      throw new RequiredAddonPathException("Addon '" + jar.getName() + "' doesn't have a version on " +
        addonConfigFileName);
    }
    if (mainClass == null) {
      throw new RequiredAddonPathException("Addon '" + jar.getName() + "' doesn't have a main class on " +
        addonConfigFileName);
    }
    return new AddonDescription(name, version, mainClass, data);
  }

  /**
   * Get the name of the addon
   *
   * @return the name
   */
  @NotNull
  public String getName() {
    return this.name;
  }

  /**
   * Get the version of the addon
   *
   * @return the version
   */
  @NotNull
  public String getVersion() {
    return this.version;
  }

  /**
   * Get the main class of the addon
   *
   * @return the path to the main class
   */
  @NotNull
  public String getMainClass() {
    return this.mainClass;
  }

  /**
   * Get the addon config file
   *
   * @return the file
   */
  @NotNull
  public FileConfiguration getConfiguration() {
    return this.configuration;
  }
}
