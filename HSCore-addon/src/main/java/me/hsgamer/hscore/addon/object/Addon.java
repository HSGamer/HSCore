package me.hsgamer.hscore.addon.object;

import me.hsgamer.hscore.addon.AddonManager;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.yaml.YamlConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The main class of the addon
 */
public abstract class Addon {

  private final AddonClassLoader addonClassLoader;
  private Config addonConfig;
  private File dataFolder;

  public Addon() {
    this.addonClassLoader = (AddonClassLoader) this.getClass().getClassLoader();
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
   * Create the config
   */
  @NotNull
  protected Config createConfig() {
    return new YamlConfig(getDataFolder(), "config.yml");
  }

  /**
   * Setup the config
   */
  public final void setupConfig() {
    this.addonConfig = createConfig();
  }

  /**
   * Get the config
   *
   * @return the config
   */
  @NotNull
  public final FileConfiguration getConfig() {
    return getAddonConfig().getConfig();
  }

  /**
   * Get the {@link Config} of the addon
   *
   * @return the {@link Config}
   */
  @NotNull
  public final Config getAddonConfig() {
    if (this.addonConfig == null) {
      setupConfig();
    }
    return this.addonConfig;
  }

  /**
   * Reload the config
   */
  public final void reloadConfig() {
    getAddonConfig().reloadConfig();
  }

  /**
   * Save the config
   */
  public final void saveConfig() {
    getAddonConfig().saveConfig();
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
      this.dataFolder = new File(getAddonManager().getAddonsDir(), getDescription().getName());
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
  public final void saveResource(@NotNull String path, final boolean replace) {
    if (Validate.isNullOrEmpty(path)) {
      throw new IllegalArgumentException("Path cannot be null or empty");
    }

    path = path.replace('\\', '/');
    try (JarFile jar = new JarFile(this.addonClassLoader.getFile())) {
      JarEntry jarConfig = jar.getJarEntry(path);
      if (jarConfig != null) {
        try (InputStream in = jar.getInputStream(jarConfig)) {
          if (in == null) {
            throw new IllegalArgumentException(
              "The embedded resource '" + path + "' cannot be found");
          }
          File out = new File(getDataFolder(), path);
          out.getParentFile().mkdirs();
          if (!out.exists() || replace) {
            Files.copy(in, out.toPath(), StandardCopyOption.REPLACE_EXISTING);
          }
        }
      } else {
        throw new IllegalArgumentException("The embedded resource '" + path + "' cannot be found");
      }
    } catch (IOException e) {
      getAddonManager().getLogger().warning("Could not load from jar file. " + path);
    }
  }

  /**
   * Get the resource from the addon's jar
   *
   * @param path path to resource
   * @return the InputStream of the resource, or null if it's not found
   */
  @Nullable
  public final InputStream getResource(@NotNull String path) {
    if (Validate.isNullOrEmpty(path)) {
      throw new IllegalArgumentException("Path cannot be null or empty");
    }

    path = path.replace('\\', '/');
    try (JarFile jar = new JarFile(this.addonClassLoader.getFile())) {
      JarEntry jarConfig = jar.getJarEntry(path);
      if (jarConfig != null) {
        try (InputStream in = jar.getInputStream(jarConfig)) {
          return in;
        }
      }
    } catch (IOException e) {
      getAddonManager().getLogger().warning("Could not load from jar file. " + path);
    }
    return null;
  }
}
