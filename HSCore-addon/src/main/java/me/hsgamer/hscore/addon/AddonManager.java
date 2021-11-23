package me.hsgamer.hscore.addon;

import me.hsgamer.hscore.addon.object.Addon;
import me.hsgamer.hscore.addon.object.AddonClassLoader;
import me.hsgamer.hscore.addon.object.AddonDescription;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.config.ConfigProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * A class that manages all addons in it
 */
public abstract class AddonManager {

  /**
   * The addon map keyed addon's id, valued addon itself
   */
  protected final Map<String, Addon> addons = new LinkedHashMap<>();

  /**
   * The addon map keyed addon itself, valued addon's class loader
   */
  protected final Map<Addon, AddonClassLoader> loaderMap = new HashMap<>();

  /**
   * The file that contains all addons
   */
  @NotNull
  private final File addonsDir;

  /**
   * The logger to use in all addons
   */
  @NotNull
  private final Logger logger;

  /**
   * Create a new addon manager
   *
   * @param addonsDir the directory to store addon files
   * @param logger    the logger to use in every addon
   */
  protected AddonManager(@NotNull final File addonsDir, @NotNull final Logger logger) {
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
    return this.addonsDir;
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

  /**
   * Load all addons from the addon directory. Also call {@link Addon#onLoad()}
   */
  public void loadAddons() {
    final Map<String, Addon> addonMap = new HashMap<>();
    // Load the addon files
    Arrays.stream(Objects.requireNonNull(this.addonsDir.listFiles()))
      .filter(file -> file.isFile() && file.getName().endsWith(".jar"))
      .forEach(file -> {
        try (final JarFile jar = new JarFile(file)) {
          // Get addon description
          final AddonDescription addonDescription = AddonDescription.get(jar, this.getAddonConfigFileName(), this.getConfigProvider());
          if (addonMap.containsKey(addonDescription.getName())) {
            this.logger.warning("Duplicated addon " + addonDescription.getName());
            return;
          }
          // Try to load the addon
          final AddonClassLoader loader = new AddonClassLoader(this, file, addonDescription, this.getClass().getClassLoader());
          final Addon addon = loader.getAddon();
          if (this.onAddonLoading(addon)) {
            addonMap.put(addonDescription.getName(), loader.getAddon());
            this.loaderMap.put(addon, loader);
          } else {
            loader.close();
          }
        } catch (final Exception e) {
          this.logger.log(Level.WARNING, e, () -> "Error when loading " + file.getName());
        }
      });
    // Filter and sort the addons
    final Map<String, Addon> sortedAddonMap = this.sortAndFilter(addonMap);
    // Close AddonClassLoader of remaining addons
    addonMap.entrySet().stream()
      .filter(entry -> !sortedAddonMap.containsKey(entry.getKey()))
      .forEach(entry -> this.closeClassLoader(entry.getValue()));
    // Load the addons
    final Map<String, Addon> finalAddons = new LinkedHashMap<>();
    sortedAddonMap.forEach((key, addon) -> {
      try {
        if (!addon.onLoad()) {
          this.logger.warning("Failed to load " + key + " " + addon.getDescription().getVersion());
          this.closeClassLoader(addon);
          return;
        }
        this.logger.info("Loaded " + key + " " + addon.getDescription().getVersion());
        finalAddons.put(key, addon);
      } catch (final Throwable t) {
        this.logger.log(Level.WARNING, t, () -> "Error when loading " + key);
        this.closeClassLoader(addon);
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
   *
   * @return whether it's enabled successfully
   */
  public boolean enableAddon(@NotNull final String name, final boolean closeLoaderOnFailed) {
    final Addon addon = this.addons.get(name);
    try {
      addon.onEnable();
      return true;
    } catch (final Throwable t) {
      this.logger.log(Level.WARNING, t, () -> "Error when enabling " + name);
      if (closeLoaderOnFailed) {
        this.closeClassLoader(addon);
      }
      return false;
    }
  }

  /**
   * Disable (call {@link Addon#onDisable()}) the addon
   *
   * @param name                the addon name
   * @param closeLoaderOnFailed close the class loader if failed
   *
   * @return whether it's disabled successfully
   */
  public boolean disableAddon(@NotNull final String name, final boolean closeLoaderOnFailed) {
    final Addon addon = this.addons.get(name);
    try {
      addon.onDisable();
      return true;
    } catch (final Throwable t) {
      this.logger.log(Level.WARNING, t, () -> "Error when disabling " + name);
      if (closeLoaderOnFailed) {
        this.closeClassLoader(addon);
      }
      return false;
    }
  }

  /**
   * Enable all addons from the addon directory
   */
  public void enableAddons() {
    final List<String> failed = new LinkedList<>();
    this.addons.keySet().forEach(name -> {
      if (!this.enableAddon(name, true)) {
        failed.add(name);
      } else {
        this.logger.log(Level.INFO, "Enabled {0}",
          String.join(" ", name, this.addons.get(name).getDescription().getVersion()));
      }
    });
    failed.forEach(this.addons::remove);
  }

  /**
   * Call the {@link Addon#onPostEnable()} method of all enabled addons
   */
  public void callPostEnable() {
    this.addons.values().forEach(Addon::onPostEnable);
  }

  /**
   * Call the {@link Addon#onReload()} method of all enabled addons
   */
  public void callReload() {
    this.addons.values().forEach(Addon::onReload);
  }

  /**
   * Disable all enabled addons
   */
  public void disableAddons() {
    CollectionUtils.reverse(this.addons.keySet()).forEach(name -> {
      if (this.disableAddon(name, false)) {
        this.logger.log(Level.INFO, "Disabled {0}",
          String.join(" ", name, this.addons.get(name).getDescription().getVersion()));
      }
    });
    this.addons.values().forEach(this::closeClassLoader);
    this.addons.clear();
  }

  /**
   * Get the enabled addon
   *
   * @param name the name of the addon
   *
   * @return the addon, or null if it's not found
   */
  @Nullable
  public Addon getAddon(@NotNull final String name) {
    return this.addons.get(name);
  }

  /**
   * Check if the addon is loaded
   *
   * @param name the name of the addon
   *
   * @return whether it's loaded
   */
  public boolean isAddonLoaded(@NotNull final String name) {
    return this.addons.containsKey(name);
  }

  /**
   * Get all loaded addons
   *
   * @return the loaded addons
   */
  @NotNull
  public Map<String, Addon> getLoadedAddons() {
    return Collections.unmodifiableMap(this.addons);
  }

  /**
   * Get the name of the addon config file
   *
   * @return the file name
   */
  @NotNull
  public abstract String getAddonConfigFileName();

  /**
   * Find a class for an addon
   *
   * @param addon the calling addon
   * @param name  the class name
   *
   * @return the class, or null if it's not found
   */
  @Nullable
  public Class<?> findClass(@NotNull final Addon addon, @NotNull final String name) {
    return this.loaderMap.entrySet()
      .parallelStream()
      .filter(entry -> entry.getKey() != addon)
      .flatMap(entry -> Optional.ofNullable(entry.getValue().findClass(name, false)).map(Stream::of).orElse(Stream.empty()))
      .findAny()
      .orElse(null);
  }

  /**
   * Filter and sort the order of the addons
   *
   * @param original the original map
   *
   * @return the sorted and filtered map
   */
  @NotNull
  protected Map<String, Addon> sortAndFilter(@NotNull final Map<String, Addon> original) {
    return original;
  }

  /**
   * Get the addon config provider
   *
   * @return the provider
   */
  @NotNull
  public abstract ConfigProvider<?> getConfigProvider();

  /**
   * Called when the addon is on loading
   *
   * @param addon the loading addon
   *
   * @return whether the addon is properly loaded
   */
  protected boolean onAddonLoading(@NotNull final Addon addon) {
    return true;
  }

  /**
   * Close the class loader of the addon
   *
   * @param addon the addon
   */
  private void closeClassLoader(@NotNull final Addon addon) {
    if (this.loaderMap.containsKey(addon)) {
      final AddonClassLoader loader = this.loaderMap.remove(addon);
      try {
        loader.close();
      } catch (final IOException e) {
        this.logger.log(Level.WARNING, "Error when closing ClassLoader", e);
      }
    }
  }
}
