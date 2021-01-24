package me.hsgamer.hscore.addon.object;

import me.hsgamer.hscore.addon.exception.RequiredAddonPathException;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.ConfigProvider;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
  private final Config config;

  /**
   * Create an addon description
   *
   * @param name      the name of the addon
   * @param version   the version of the addon
   * @param mainClass the main class of the addon
   * @param config    the configuration of the addon
   */
  public AddonDescription(@NotNull final String name, @NotNull final String version, @NotNull final String mainClass,
                          @NotNull final Config config) {
    this.name = name;
    this.version = version;
    this.mainClass = mainClass;
    this.config = config;
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
                                     @NotNull final ConfigProvider<? extends Config> provider)
    throws IOException {
    // Load the addon config file
    final JarEntry entry = jar.getJarEntry(addonConfigFileName);
    if (entry == null) {
      throw new NoSuchFileException("Addon '" + jar.getName() + "' doesn't contain " + addonConfigFileName + " file");
    }
    final InputStream inputStream = jar.getInputStream(entry);
    final Config data = provider.loadConfiguration(inputStream);
    inputStream.close();
    // Load required descriptions
    final String name = data.getInstance("name", String.class);
    final String version = data.getInstance("version", String.class);
    final String mainClass = data.getInstance("main", String.class);
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
  public Config getConfiguration() {
    return this.config;
  }
}
