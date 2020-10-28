package me.hsgamer.hscore.addon.object;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import me.hsgamer.hscore.addon.AddonManager;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.FileConfiguration;

/**
 * The main class of the addon
 */
public abstract class Addon {

  /**
   * The addon's class loader
   */
  private final AddonClassLoader addonClassLoader;

  /**
   * The addon's config
   */
  private Config addonConfig;

  /**
   * The addon's data folder
   */
  private File dataFolder;

  /**
   * Create an addon
   */
  public Addon() {
    this.addonClassLoader = (AddonClassLoader) this.getClass().getClassLoader();
  }

  /**
   * Called when loading the addon
   *
   * @return whether the addon loaded properly
   */
  public boolean onLoad() {
    return true;
  }

  /**
   * Called when enabling the addon
   */
  public void onEnable() {
    // EMPTY
  }

  /**
   * Called after all addons enabled
   */
  public void onPostEnable() {
    // EMPTY
  }

  /**
   * Called when disabling the addon
   */
  public void onDisable() {
    // EMPTY
  }

  /**
   * Called when reloading
   */
  public void onReload() {
    // EMPTY
  }

  /**
   * Get the addon's description
   *
   * @return the description
   */
  @NotNull
  public final AddonDescription getDescription() {
    return this.addonClassLoader.getAddonDescription();
  }

  /**
   * Setup the config
   */
  public final void setupConfig() {
    this.addonConfig = this.createConfig();
  }

  /**
   * Get the config
   *
   * @return the config
   */
  @NotNull
  public final FileConfiguration getConfig() {
    return this.getAddonConfig().getConfig();
  }

  /**
   * Get the {@link Config} of the addon
   *
   * @return the {@link Config}
   */
  @NotNull
  public final Config getAddonConfig() {
    if (this.addonConfig == null) {
      this.setupConfig();
    }
    return this.addonConfig;
  }

  /**
   * Reload the config
   */
  public final void reloadConfig() {
    this.getAddonConfig().reloadConfig();
  }

  /**
   * Save the config
   */
  public final void saveConfig() {
    this.getAddonConfig().saveConfig();
  }

  /**
   * Get the addon manager
   *
   * @return the addon manager
   */
  @NotNull
  public final AddonManager getAddonManager() {
    return this.addonClassLoader.getAddonManager();
  }

  /**
   * Get the addon's folder
   *
   * @return the directory for the addon
   */
  @NotNull
  public final File getDataFolder() {
    if (this.dataFolder == null) {
      this.dataFolder = new File(this.getAddonManager().getAddonsDir(), this.getDescription().getName());
    }
    if (!this.dataFolder.exists()) {
      this.dataFolder.mkdirs();
    }
    return this.dataFolder;
  }

  /**
   * Copy the resource from the addon's jar
   *
   * @param path    path to resource
   * @param replace whether it replaces the existed one
   */
  public final void saveResource(@NotNull final String path, final boolean replace) {
    if (Validate.isNullOrEmpty(path)) {
      throw new IllegalArgumentException("Path cannot be null or empty");
    }
    final String newPath = path.replace('\\', '/');
    try (final JarFile jar = new JarFile(this.addonClassLoader.getFile())) {
      final JarEntry jarConfig = jar.getJarEntry(newPath);
      if (jarConfig != null) {
        try (final InputStream in = jar.getInputStream(jarConfig)) {
          if (in == null) {
            throw new IllegalArgumentException(
              "The embedded resource '" + newPath + "' cannot be found");
          }
          final File out = new File(this.getDataFolder(), newPath);
          out.getParentFile().mkdirs();
          if (!out.exists() || replace) {
            Files.copy(in, out.toPath(), StandardCopyOption.REPLACE_EXISTING);
          }
        }
      } else {
        throw new IllegalArgumentException("The embedded resource '" + newPath + "' cannot be found");
      }
    } catch (final IOException e) {
      this.getAddonManager().getLogger().warning("Could not load from jar file. " + newPath);
    }
  }

  /**
   * Get the resource from the addon's jar
   *
   * @param path path to resource
   *
   * @return the InputStream of the resource, or null if it's not found
   */
  @Nullable
  public final InputStream getResource(@NotNull final String path) {
    if (Validate.isNullOrEmpty(path)) {
      throw new IllegalArgumentException("Path cannot be null or empty");
    }
    final String newPath = path.replace('\\', '/');
    try (final JarFile jar = new JarFile(this.addonClassLoader.getFile())) {
      final JarEntry jarConfig = jar.getJarEntry(newPath);
      if (jarConfig != null) {
        try (final InputStream in = jar.getInputStream(jarConfig)) {
          return in;
        }
      }
    } catch (final IOException e) {
      this.getAddonManager().getLogger().warning("Could not load from jar file. " + newPath);
    }
    return null;
  }

  /**
   * Get the class loader
   *
   * @return the class loader
   */
  @NotNull
  protected final AddonClassLoader getClassLoader() {
    return this.addonClassLoader;
  }

  /**
   * Create the config
   */
  @NotNull
  protected abstract Config createConfig();
}
