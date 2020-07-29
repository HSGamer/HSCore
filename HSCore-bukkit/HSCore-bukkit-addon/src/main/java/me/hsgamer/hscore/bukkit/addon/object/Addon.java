package me.hsgamer.hscore.bukkit.addon.object;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import me.hsgamer.hscore.bukkit.addon.AddonManager;
import me.hsgamer.hscore.bukkit.config.PluginConfig;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the addon
 */
public abstract class Addon {

  private final File jarFile;
  private final AddonClassLoader addonClassLoader;
  private AddonManager addonManager;
  private File dataFolder;
  private PluginConfig config;
  private AddonDescription description;

  public Addon() {
    this.addonClassLoader = (AddonClassLoader) this.getClass().getClassLoader();
    this.jarFile = addonClassLoader.getFile();
  }

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
   * Get the parent plugin
   *
   * @return the plugin
   */
  public JavaPlugin getPlugin() {
    return addonManager.getPlugin();
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
   * Get the addon manager
   *
   * @return the addon manager
   */
  public final AddonManager getAddonManager() {
    return addonManager;
  }

  public final void setAddonManager(AddonManager addonManager) {
    this.addonManager = addonManager;
  }

  /**
   * Create the config
   */
  public final void setupConfig() {
    config = new PluginConfig(getPlugin(), new File(getDataFolder(), "config.yml"));
  }

  /**
   * Get the config
   *
   * @return the config
   */
  public final FileConfiguration getConfig() {
    if (config == null) {
      setupConfig();
    }
    return config.getConfig();
  }

  /**
   * Reload the config
   */
  public final void reloadConfig() {
    if (config == null) {
      setupConfig();
    } else {
      config.reloadConfig();
    }
  }

  /**
   * Save the config
   */
  public final void saveConfig() {
    if (config == null) {
      setupConfig();
    } else {
      config.saveConfig();
    }
  }

  /**
   * Get the addon's folder
   *
   * @return the directory for the addon
   */
  public final File getDataFolder() {
    if (dataFolder == null) {
      dataFolder = new File(getPlugin().getDataFolder(),
          "addon" + File.separator + description.getName());
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
    try (JarFile jar = new JarFile(jarFile)) {
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
      getPlugin().getLogger().warning("Could not load from jar file. " + path);
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
    try (JarFile jar = new JarFile(jarFile)) {
      JarEntry jarConfig = jar.getJarEntry(path);
      if (jarConfig != null) {
        try (InputStream in = jar.getInputStream(jarConfig)) {
          return in;
        }
      }
    } catch (IOException e) {
      getPlugin().getLogger().warning("Could not load from jar file. " + path);
    }
    return null;
  }
}
