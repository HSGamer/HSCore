package me.hsgamer.hscore.expansion.common.manager;

import me.hsgamer.hscore.expansion.common.factory.ExpansionFactory;
import me.hsgamer.hscore.expansion.common.loader.ExpansionDescriptionLoader;
import me.hsgamer.hscore.expansion.common.object.Expansion;
import me.hsgamer.hscore.expansion.common.object.ExpansionClassLoader;
import me.hsgamer.hscore.expansion.common.object.ExpansionDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class that manages all addons in it
 */
public class ExpansionManager<T extends Expansion> {

  /**
   * The class loader map keyed addon's id, valued addon's class loader
   */
  protected final Map<String, ExpansionClassLoader<T>> classLoaders = new LinkedHashMap<>();

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
   * The parent class loader to load all addons
   */
  @NotNull
  private final ClassLoader parentClassLoader;

  /**
   * The addon description loader
   */
  @NotNull
  private final ExpansionDescriptionLoader addonDescriptionLoader;

  @NotNull
  private final ExpansionFactory<T> expansionFactory;

  /**
   * Create a new addon manager
   *
   * @param addonsDir              the directory to store addon files
   * @param logger                 the logger to use in every addon
   * @param addonDescriptionLoader the loader to load addon description
   * @param expansionFactory
   */
  protected ExpansionManager(@NotNull final File addonsDir, @NotNull final Logger logger, @NotNull ExpansionDescriptionLoader addonDescriptionLoader, @NotNull ExpansionFactory<T> expansionFactory) {
    this(addonsDir, logger, addonDescriptionLoader, expansionFactory, ExpansionManager.class.getClassLoader());
  }

  /**
   * Create a new addon manager
   *
   * @param addonsDir              the directory to store addon files
   * @param logger                 the logger to use in every addon
   * @param addonDescriptionLoader the loader to load addon description
   * @param expansionFactory
   * @param parentClassLoader      the parent class loader to load all addons
   */
  public ExpansionManager(@NotNull final File addonsDir, @NotNull final Logger logger, @NotNull ExpansionDescriptionLoader addonDescriptionLoader, @NotNull ExpansionFactory<T> expansionFactory, @NotNull final ClassLoader parentClassLoader) {
    this.logger = logger;
    this.addonsDir = addonsDir;
    this.addonDescriptionLoader = addonDescriptionLoader;
    this.expansionFactory = expansionFactory;
    this.parentClassLoader = parentClassLoader;
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
   * Get the addon description loader
   *
   * @return the loader
   */
  @NotNull
  public ExpansionDescriptionLoader getAddonDescriptionLoader() {
    return addonDescriptionLoader;
  }

  @NotNull
  public ExpansionFactory<T> getExpansionFactory() {
    return expansionFactory;
  }

  /**
   * Load all addons from the addon directory. Also call {@link Expansion#onLoad()}
   */
  public void loadAddons() {
    final Map<String, ExpansionClassLoader<T>> classLoaderMap = new HashMap<>();
    // Load the addon files
    Arrays.stream(Objects.requireNonNull(this.addonsDir.listFiles()))
      .filter(file -> file.isFile() && file.getName().toLowerCase(Locale.ROOT).endsWith(".jar"))
      .forEach(file -> {
        try (final JarFile jar = new JarFile(file)) {
          // Get addon description
          final ExpansionDescription addonDescription = addonDescriptionLoader.load(jar);
          if (classLoaderMap.containsKey(addonDescription.getName())) {
            this.logger.warning("Duplicated addon " + addonDescription.getName());
            return;
          }
          // Try to load the addon
          final ExpansionClassLoader<T> loader = new ExpansionClassLoader<>(this, file, addonDescription, this.parentClassLoader);
          final T addon = loader.getAddon();
          if (this.onAddonLoading(addon)) {
            classLoaderMap.put(addonDescription.getName(), loader);
          } else {
            loader.close();
          }
        } catch (final Exception e) {
          this.logger.log(Level.WARNING, e, () -> "Error when loading " + file.getName());
        }
      });
    // Filter and sort the addons
    final Map<String, ExpansionClassLoader<T>> sortedClassLoaderMap = this.sortAndFilter(classLoaderMap);
    // Close AddonClassLoader of remaining addons
    classLoaderMap.entrySet().stream()
      .filter(entry -> !sortedClassLoaderMap.containsKey(entry.getKey()))
      .forEach(entry -> this.closeClassLoaderSafe(entry.getValue()));
    // Load the addons
    final Map<String, ExpansionClassLoader<T>> finalClassLoaders = new LinkedHashMap<>();
    sortedClassLoaderMap.forEach((key, loader) -> {
      try {
        final T addon = loader.getAddon();
        if (!addon.onLoad()) {
          this.logger.warning("Failed to load " + key + " " + loader.getAddonDescription().getVersion());
          this.closeClassLoaderSafe(loader);
          return;
        }
        this.logger.info("Loaded " + key + " " + loader.getAddonDescription().getVersion());
        finalClassLoaders.put(key, loader);
      } catch (final Throwable t) {
        this.logger.log(Level.WARNING, t, () -> "Error when loading " + key);
        this.closeClassLoaderSafe(loader);
      }
    });
    // Store the final addons map
    this.classLoaders.putAll(finalClassLoaders);
  }

  /**
   * Enable (call {@link Expansion#onEnable()}) the addon
   *
   * @param name                the addon name
   * @param closeLoaderOnFailed close the class loader if failed
   *
   * @return whether it's enabled successfully
   */
  public boolean enableAddon(@NotNull final String name, final boolean closeLoaderOnFailed) {
    try {
      final T addon = classLoaders.get(name).getAddon();
      this.onAddonEnable(addon);
      addon.onEnable();
      this.onAddonEnabled(addon);
      return true;
    } catch (final Throwable t) {
      this.logger.log(Level.WARNING, t, () -> "Error when enabling " + name);
      if (closeLoaderOnFailed) {
        this.closeClassLoaderSafe(name);
      }
      return false;
    }
  }

  /**
   * Disable (call {@link Expansion#onDisable()}) the addon
   *
   * @param name                the addon name
   * @param closeLoaderOnFailed close the class loader if failed
   *
   * @return whether it's disabled successfully
   */
  public boolean disableAddon(@NotNull final String name, final boolean closeLoaderOnFailed) {
    try {
      final T addon = classLoaders.get(name).getAddon();
      this.onAddonDisable(addon);
      addon.onDisable();
      this.onAddonDisabled(addon);
      return true;
    } catch (final Throwable t) {
      this.logger.log(Level.WARNING, t, () -> "Error when disabling " + name);
      if (closeLoaderOnFailed) {
        this.closeClassLoaderSafe(name);
      }
      return false;
    }
  }

  /**
   * Enable all addons from the addon directory
   */
  public void enableAddons() {
    final List<String> failed = new LinkedList<>();
    this.classLoaders.keySet().forEach(name -> {
      if (!this.enableAddon(name, true)) {
        failed.add(name);
      } else {
        this.logger.log(Level.INFO, "Enabled {0}", String.join(" ", name, this.classLoaders.get(name).getAddonDescription().getVersion()));
      }
    });
    failed.forEach(this.classLoaders::remove);
  }

  /**
   * Call the consumer for all enabled addon
   *
   * @param consumer the consumer
   */
  public void call(Consumer<T> consumer) {
    this.classLoaders.values().forEach(classLoader -> consumer.accept(classLoader.getAddon()));
  }

  /**
   * Disable all enabled addons
   */
  public void disableAddons() {
    Deque<String> stack = new LinkedList<>(this.classLoaders.keySet());
    while (true) {
      final String name = stack.pollLast();
      if (name == null) break;
      if (this.disableAddon(name, false)) {
        this.logger.log(Level.INFO, "Disabled {0} {1}", new Object[]{name, this.classLoaders.get(name).getAddonDescription().getVersion()});
      }
    }
    this.classLoaders.values().forEach(this::closeClassLoaderSafe);
    this.classLoaders.clear();
  }

  /**
   * Get the enabled addon
   *
   * @param name the name of the addon
   *
   * @return the addon, or null if it's not found
   */
  @Nullable
  public T getAddon(@NotNull final String name) {
    final ExpansionClassLoader<T> classLoader = this.classLoaders.get(name);
    return classLoader == null ? null : classLoader.getAddon();
  }

  /**
   * Check if the addon is loaded
   *
   * @param name the name of the addon
   *
   * @return whether it's loaded
   */
  public boolean isAddonLoaded(@NotNull final String name) {
    return this.classLoaders.containsKey(name);
  }

  /**
   * Get all loaded addons
   *
   * @return the loaded addons
   */
  @NotNull
  public Map<String, T> getLoadedAddons() {
    return this.classLoaders.entrySet()
      .parallelStream()
      .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getAddon()));
  }

  /**
   * Find a class for an addon
   *
   * @param classLoader the calling class loader
   * @param name        the class name
   *
   * @return the class, or null if it's not found
   */
  @Nullable
  public Class<?> findClass(@NotNull final ExpansionClassLoader<T> classLoader, @NotNull final String name) {
    return this.classLoaders.entrySet()
      .parallelStream()
      .filter(entry -> entry.getValue() != classLoader)
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
  protected Map<String, ExpansionClassLoader<T>> sortAndFilter(@NotNull final Map<String, ExpansionClassLoader<T>> original) {
    return original;
  }

  /**
   * Called when the addon is on loading
   *
   * @param addon the loading addon
   *
   * @return whether the addon is properly loaded
   */
  protected boolean onAddonLoading(@NotNull final T addon) {
    return true;
  }

  /**
   * Called when the addon is on enable
   *
   * @param addon the enabling addon
   */
  protected void onAddonEnable(@NotNull final T addon) {
    // EMPTY
  }

  /**
   * Called when the addon is enabled
   *
   * @param addon the enabled addon
   */
  protected void onAddonEnabled(@NotNull final T addon) {
    // EMPTY
  }

  /**
   * Called when the addon is on disabling
   *
   * @param addon the disabling addon
   */
  protected void onAddonDisable(@NotNull final T addon) {
    // EMPTY
  }

  /**
   * Called when the addon is disabled
   *
   * @param addon the disabled addon
   */
  protected void onAddonDisabled(@NotNull final T addon) {
    // EMPTY
  }

  /**
   * Close the class loader safely
   *
   * @param classLoader the class loader
   */
  private void closeClassLoaderSafe(@NotNull final ExpansionClassLoader<T> classLoader) {
    try {
      classLoader.close();
    } catch (final IOException e) {
      this.logger.log(Level.WARNING, "Error when closing ClassLoader", e);
    }
  }

  /**
   * Close the class loader of the addon
   *
   * @param name the addon name
   */
  private void closeClassLoaderSafe(@NotNull final String name) {
    ExpansionClassLoader<T> loader = this.classLoaders.get(name);
    if (loader != null) {
      this.closeClassLoaderSafe(loader);
    }
  }
}
