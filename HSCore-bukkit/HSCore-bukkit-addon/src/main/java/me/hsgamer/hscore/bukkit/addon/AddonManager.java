package me.hsgamer.hscore.bukkit.addon;

import me.hsgamer.hscore.bukkit.addon.object.Addon;
import me.hsgamer.hscore.bukkit.addon.object.AddonClassLoader;
import me.hsgamer.hscore.bukkit.addon.object.AddonDescription;
import me.hsgamer.hscore.common.CommonUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Level;

/**
 * An Addon manager
 */
public abstract class AddonManager {

  private final Map<String, Addon> addons = new LinkedHashMap<>();
  private final Map<Addon, AddonClassLoader> loaderMap = new HashMap<>();
  private final File addonsDir;
  private final JavaPlugin plugin;

  /**
   * Create a new addon manager
   *
   * @param plugin the parent plugin
   */
  public AddonManager(JavaPlugin plugin) {
    this.plugin = plugin;
    addonsDir = new File(plugin.getDataFolder(), "addon");
    if (!addonsDir.exists()) {
      addonsDir.mkdirs();
    }
  }

  /**
   * Get the parent plugin
   *
   * @return the plugin
   */
  public JavaPlugin getPlugin() {
    return plugin;
  }

  /**
   * Get the addon directory
   *
   * @return the directory
   */
  public File getAddonsDir() {
    return addonsDir;
  }

  /**
   * Load all addons from the addon directory. Also call {@link Addon#onLoad() onLoad()}
   */
  public void loadAddons() {
    Map<String, Addon> addonMap = new HashMap<>();

    // Load the addon files
    Arrays.stream(Objects.requireNonNull(addonsDir.listFiles()))
      .filter(file -> file.isFile() && file.getName().endsWith(".jar"))
      .forEach(file -> {
        try (JarFile jar = new JarFile(file)) {
          // Get addon description
          AddonDescription addonDescription = AddonDescription.get(jar);
          if (addonMap.containsKey(addonDescription.getName())) {
            plugin.getLogger().warning("Duplicated addon " + addonDescription.getName());
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
          plugin.getLogger().log(Level.WARNING, "Error when loading jar", e);
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
          plugin.getLogger().warning(
            "Failed to load " + key + " " + addon.getDescription().getVersion());
          closeClassLoader(addon);
          return;
        }

        plugin.getLogger()
          .info("Loaded " + key + " " + addon.getDescription().getVersion());
        finalAddons.put(key, addon);
      } catch (Throwable t) {
        plugin.getLogger().log(Level.WARNING, t, () -> "Error when loading " + key);
        closeClassLoader(addon);
      }
    });

    // Store the final addons map
    addons.putAll(finalAddons);
  }

  /**
   * Enable (call {@link Addon#onEnable() onEnable()}) the addon
   *
   * @param name                the addon name
   * @param closeLoaderOnFailed close the class loader if failed
   * @return whether it's enabled successfully
   */
  public boolean enableAddon(String name, boolean closeLoaderOnFailed) {
    Addon addon = addons.get(name);
    try {
      addon.onEnable();
      return true;
    } catch (Throwable t) {
      plugin.getLogger().log(Level.WARNING, t, () -> "Error when enabling " + name);
      if (closeLoaderOnFailed) {
        closeClassLoader(addon);
      }
      return false;
    }
  }

  /**
   * Disable (call {@link Addon#onDisable() onDisable()}) the addon
   *
   * @param name                the addon name
   * @param closeLoaderOnFailed close the class loader if failed
   * @return whether it's disabled successfully
   */
  public boolean disableAddon(String name, boolean closeLoaderOnFailed) {
    Addon addon = addons.get(name);
    try {
      addon.onDisable();
      return true;
    } catch (Throwable t) {
      plugin.getLogger().log(Level.WARNING, t, () -> "Error when disabling " + name);
      if (closeLoaderOnFailed) {
        closeClassLoader(addon);
      }
      return false;
    }
  }

  /**
   * Enable all addons from the addon directory
   */
  public void enableAddons() {
    List<String> failed = new ArrayList<>();
    addons.keySet().forEach(name -> {
      if (!enableAddon(name, true)) {
        failed.add(name);
      } else {
        plugin.getLogger().log(Level.INFO, "Enabled {0}",
          String.join(" ", name, addons.get(name).getDescription().getVersion()));
      }
    });
    failed.forEach(addons::remove);
  }

  /**
   * Call the {@link Addon#onPostEnable() onPostEnable()} method of all enabled addons
   */
  public void callPostEnable() {
    addons.values().forEach(Addon::onPostEnable);
  }

  /**
   * Call the {@link Addon#onReload() onReload()} method of all enabled addons
   */
  public void callReload() {
    addons.values().forEach(Addon::onReload);
  }

  /**
   * Disable all enabled addons
   */
  public void disableAddons() {
    CommonUtils.reverse(addons.keySet()).forEach(name -> {
      if (disableAddon(name, false)) {
        plugin.getLogger().log(Level.INFO, "Disabled {0}",
          String.join(" ", name, addons.get(name).getDescription().getVersion()));
      }
    });

    addons.values().forEach(this::closeClassLoader);
    addons.clear();
  }

  /**
   * Close the class loader of the addon
   *
   * @param addon the addon
   */
  private void closeClassLoader(Addon addon) {
    if (loaderMap.containsKey(addon)) {
      AddonClassLoader loader = loaderMap.remove(addon);
      try {
        loader.close();
      } catch (IOException e) {
        plugin.getLogger().log(Level.WARNING, "Error when closing ClassLoader", e);
      }
    }
  }

  /**
   * Get the enabled addon
   *
   * @param name the name of the addon
   * @return the addon, or null if it's not found
   */
  public Addon getAddon(String name) {
    return addons.get(name);
  }

  /**
   * Check if the addon is loaded
   *
   * @param name the name of the addon
   * @return whether it's loaded
   */
  public boolean isAddonLoaded(String name) {
    return addons.containsKey(name);
  }

  /**
   * Get all loaded addons
   *
   * @return the loaded addons
   */
  public Map<String, Addon> getLoadedAddons() {
    return addons;
  }

  /**
   * Filter and sort the order of the addons
   *
   * @param original the original map
   * @return the sorted and filtered map
   */
  protected abstract Map<String, Addon> sortAndFilter(Map<String, Addon> original);

  /**
   * Called when the addon is on loading
   *
   * @param addon the loading addon
   * @return whether the addon is properly loaded
   */
  protected boolean onAddonLoading(Addon addon) {
    return true;
  }

  /**
   * Find a class for an addon
   *
   * @param addon the calling addon
   * @param name  the class name
   * @return the class, or null if it's not found
   */
  public Class<?> findClass(Addon addon, String name) {
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
   * Get the addon count
   *
   * @return the addon count
   */
  public Map<String, Integer> getAddonCount() {
    Map<String, Integer> map = new HashMap<>();
    addons.keySet().forEach(s -> map.put(s, 1));
    return map;
  }
}
