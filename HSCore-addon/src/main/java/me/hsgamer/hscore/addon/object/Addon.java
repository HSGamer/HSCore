package me.hsgamer.hscore.addon.object;

import me.hsgamer.config.yaml.YamlConfig;
import me.hsgamer.hscore.addon.AddonManager;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.config.Config;
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
  private File dataFolder;
  private Config addonConfig;
  private AddonDescription description;

  public Addon() {
    this.addonClassLoader = (AddonClassLoader) this.getClass().getClassLoader();
  }

  /**
   * Get the class loader
   *
   * @return the class loader
   */
  protected final AddonClassLoader getClassLoader() {
    return addonClassLoader;
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
  public final AddonDescription getDescription() {
    return description;
  }

  public final void setDescription(AddonDescription description) {
    this.description = description;
  }

  /**
   * Create the config
   */
  public final void setupConfig() {
    addonConfig = new YamlConfig(getDataFolder(), "config.yml");
  }

  /**
   * Get the config
   *
   * @return the config
   */
  public final FileConfiguration getConfig() {
    return getAddonConfig().getConfig();
  }

  /**
   * Get the {@link Config} of the addon
   *
   * @return the {@link Config}
   */
  public final Config getAddonConfig() {
    if (addonConfig == null) {
      setupConfig();
    }
    return addonConfig;
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
  public final AddonManager getAddonManager() {
    return addonClassLoader.getAddonManager();
  }

  /**
   * Get the addon's folder
   *
   * @return the directory for the addon
   */
  public final File getDataFolder() {
    if (dataFolder == null) {
      dataFolder = new File(getAddonManager().getAddonsDir(), description.getName());
    }
    if (!dataFolder.exists()) {
      dataFolder.mkdirs();
    }
    return dataFolder;
  }

  /**
   * Copy the resource from the addon's jar
   *
   * @param path    path to resource
   * @param replace whether it replaces the existed one
   */
  public final void saveResource(String path, boolean replace) {
    if (Validate.isNullOrEmpty(path)) {
      throw new IllegalArgumentException("Path cannot be null or empty");
    }

    path = path.replace('\\', '/');
    try (JarFile jar = new JarFile(getClassLoader().getFile())) {
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
  public final InputStream getResource(String path) {
    if (Validate.isNullOrEmpty(path)) {
      throw new IllegalArgumentException("Path cannot be null or empty");
    }

    path = path.replace('\\', '/');
    try (JarFile jar = new JarFile(getClassLoader().getFile())) {
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
