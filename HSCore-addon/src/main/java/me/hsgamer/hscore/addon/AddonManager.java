package me.hsgamer.hscore.addon;

import me.hsgamer.hscore.addon.object.Addon;
import me.hsgamer.hscore.addon.object.AddonClassLoader;
import me.hsgamer.hscore.addon.object.AddonDescription;
import me.hsgamer.hscore.common.CommonUtils;
import me.hsgamer.hscore.config.ConfigProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An Addon manager
 */
public abstract class AddonManager {

  private final Map<String, Addon> addons = new LinkedHashMap<>();
  private final Map<Addon, AddonClassLoader> loaderMap = new HashMap<>();
  private final File addonsDir;
  private final Logger logger;

  /**
   * Create a new addon manager
   *
   * @param addonsDir the directory to store addon files
   * @param logger    the logger
   */
  public AddonManager(@NotNull final File addonsDir, @NotNull final Logger logger) {
    this.logger = logger;
    this.addonsDir = addonsDir;
    if (!addonsDir.exists()) {
      addonsDir.mkdirs();
    }
  }

  /**
   * Get the addon directory
   *
   * @return the directory
   */
  @NotNull
  public final File getAddonsDir() {
    return addonsDir;
  }

  /**
   * Load all addons from the addon directory. Also call {@link Addon#onLoad()}
   */
  public final void loadAddons() {
    Map<String, Addon> addonMap = new HashMap<>();

    // Load the addon files
    Arrays.stream(Objects.requireNonNull(addonsDir.listFiles()))
      .filter(file -> file.isFile() && file.getName().endsWith(".jar"))
      .forEach(file -> {
        try (JarFile jar = new JarFile(file)) {
          // Get addon description
          AddonDescription addonDescription = AddonDescription.get(jar, getAddonConfigFileName(), getConfigProvider());
          if (addonMap.containsKey(addonDescription.getName())) {
            logger.warning("Duplicated addon " + addonDescription.getName());
            return;
          }

          // Try to load the addon
          AddonClassLoader loader = new AddonClassLoader(this, file, addonDescription,
            getClass().getClassLoader());
          Addon addon = loader.getAddon();

          if (onAddonLoading(addon)) {
            addonMap.put(addonDescription.getName(), loader.getAddon());
            loaderMap.put(addon, loader);
          } else {
            loader.close();
          }
        } catch (Exception e) {
          logger.log(Level.WARNING, "Error when loading jar", e);
        }
      });

    // Filter and sort the addons
    Map<String, Addon> sortedAddonMap = sortAndFilter(addonMap);

    // Close AddonClassLoader of remaining addons
    addonMap.entrySet().stream()
      .filter(entry -> !sortedAddonMap.containsKey(entry.getKey()))
      .forEach(entry -> closeClassLoader(entry.getValue()));

    // Load the addons
    Map<String, Addon> finalAddons = new LinkedHashMap<>();
    sortedAddonMap.forEach((key, addon) -> {
      try {
        if (!addon.onLoad()) {
          logger.warning("Failed to load " + key + " " + addon.getDescription().getVersion());
          closeClassLoader(addon);
          return;
        }

        logger.info("Loaded " + key + " " + addon.getDescription().getVersion());
        finalAddons.put(key, addon);
      } catch (Throwable t) {
        logger.log(Level.WARNING, t, () -> "Error when loading " + key);
        closeClassLoader(addon);
      }
    });

    // Store the final addons map
    this.addons.putAll(finalAddons);
  }

  /**
   * Enable (call {@link Addon#onEnable()}) the addon
   *
   * @param name                the addon name
   * @param closeLoaderOnFailed close the class loader if failed
   * @return whether it's enabled successfully
   */
  public final boolean enableAddon(@NotNull final String name, final boolean closeLoaderOnFailed) {
    Addon addon = this.addons.get(name);
    try {
      addon.onEnable();
      return true;
    } catch (Throwable t) {
      logger.log(Level.WARNING, t, () -> "Error when enabling " + name);
      if (closeLoaderOnFailed) {
        closeClassLoader(addon);
      }
      return false;
    }
  }

  /**
   * Disable (call {@link Addon#onDisable()}) the addon
   *
   * @param name                the addon name
   * @param closeLoaderOnFailed close the class loader if failed
   * @return whether it's disabled successfully
   */
  public final boolean disableAddon(@NotNull final String name, final boolean closeLoaderOnFailed) {
    Addon addon = this.addons.get(name);
    try {
      addon.onDisable();
      return true;
    } catch (Throwable t) {
      logger.log(Level.WARNING, t, () -> "Error when disabling " + name);
      if (closeLoaderOnFailed) {
        closeClassLoader(addon);
      }
      return false;
    }
  }

  /**
   * Enable all addons from the addon directory
   */
  public final void enableAddons() {
    List<String> failed = new ArrayList<>();
    this.addons.keySet().forEach(name -> {
      if (!enableAddon(name, true)) {
        failed.add(name);
      } else {
        logger.log(Level.INFO, "Enabled {0}", String.join(" ", name, this.addons.get(name).getDescription().getVersion()));
      }
    });
    failed.forEach(this.addons::remove);
  }

  /**
   * Call the {@link Addon#onPostEnable()} method of all enabled addons
   */
  public final void callPostEnable() {
    this.addons.values().forEach(Addon::onPostEnable);
  }

  /**
   * Call the {@link Addon#onReload()} method of all enabled addons
   */
  public final void callReload() {
    this.addons.values().forEach(Addon::onReload);
  }

  /**
   * Disable all enabled addons
   */
  public final void disableAddons() {
    CommonUtils.reverse(this.addons.keySet()).forEach(name -> {
      if (disableAddon(name, false)) {
        logger.log(Level.INFO, "Disabled {0}", String.join(" ", name, this.addons.get(name).getDescription().getVersion()));
      }
    });

    this.addons.values().forEach(this::closeClassLoader);
    this.addons.clear();
  }

  /**
   * Close the class loader of the addon
   *
   * @param addon the addon
   */
  private void closeClassLoader(@NotNull final Addon addon) {
    if (loaderMap.containsKey(addon)) {
      AddonClassLoader loader = loaderMap.remove(addon);
      try {
        loader.close();
      } catch (IOException e) {
        logger.log(Level.WARNING, "Error when closing ClassLoader", e);
      }
    }
  }

  /**
   * Get the enabled addon
   *
   * @param name the name of the addon
   * @return the addon, or null if it's not found
   */
  @Nullable
  public final Addon getAddon(@NotNull final String name) {
    return this.addons.get(name);
  }

  /**
   * Check if the addon is loaded
   *
   * @param name the name of the addon
   * @return whether it's loaded
   */
  public final boolean isAddonLoaded(@NotNull final String name) {
    return this.addons.containsKey(name);
  }

  /**
   * Get all loaded addons
   *
   * @return the loaded addons
   */
  @NotNull
  public final Map<String, Addon> getLoadedAddons() {
    return this.addons;
  }

  /**
   * Filter and sort the order of the addons
   *
   * @param original the original map
   * @return the sorted and filtered map
   */
  @NotNull
  protected abstract Map<String, Addon> sortAndFilter(@NotNull final Map<String, Addon> original);

  /**
   * Get the addon config provider
   *
   * @param <T> the {@link FileConfiguration} type
   * @return the provider
   */
  @NotNull
  protected abstract <T extends FileConfiguration> ConfigProvider<T> getConfigProvider();

  /**
   * Get the name of the addon config file
   *
   * @return the file name
   */
  @NotNull
  public abstract String getAddonConfigFileName();

  /**
   * Called when the addon is on loading
   *
   * @param addon the loading addon
   * @return whether the addon is properly loaded
   */
  protected boolean onAddonLoading(@NotNull final Addon addon) {
    return true;
  }

  /**
   * Find a class for an addon
   *
   * @param addon the calling addon
   * @param name  the class name
   * @return the class, or null if it's not found
   */
  @Nullable
  public Class<?> findClass(@NotNull final Addon addon, @NotNull final String name) {
    for (AddonClassLoader loader : loaderMap.values()) {
      if (loaderMap.containsKey(addon)) {
        continue;
      }
      Class<?> clazz = loader.findClass(name, false);
      if (clazz != null) {
        return clazz;
      }
    }
    return null;
  }

  /**
   * Get the logger
   *
   * @return the logger
   */
  @NotNull
  public final Logger getLogger() {
    return this.logger;
  }
}
