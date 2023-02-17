package me.hsgamer.hscore.expansion.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class that manages all addons in it
 */
public class ExpansionManager {
  public static final Function<ExpansionClassLoader, Expansion> DEFAULT_EXTENSION_FACTORY = classLoader -> {
    try {
      final Class<?> clazz = Class.forName(classLoader.getAddonDescription().getMainClass(), true, classLoader);
      final Class<? extends Expansion> newClass;
      if (Expansion.class.isAssignableFrom(clazz)) {
        newClass = clazz.asSubclass(Expansion.class);
      } else {
        throw new ClassCastException("The main class does not extend " + Expansion.class.getSimpleName());
      }
      return newClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new IllegalStateException("Cannot create a new instance of the main class", e);
    }
  };

  /**
   * The class loader map keyed addon's id, valued addon's class loader
   */
  protected final Map<String, ExpansionClassLoader> classLoaders = new LinkedHashMap<>();

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
  private final Function<JarFile, ExpansionDescription> descriptionFactory;

  @NotNull
  private final Function<ExpansionClassLoader, Expansion> expansionFactory;

  @NotNull
  private final Set<ExpansionStateListener> stateListeners = new HashSet<>();

  /**
   * Create a new addon manager
   *
   * @param addonsDir          the directory to store addon files
   * @param logger             the logger to use in every addon
   * @param descriptionFactory the loader to load addon description
   * @param expansionFactory
   * @param parentClassLoader  the parent class loader to load all addons
   */
  public ExpansionManager(@NotNull final File addonsDir, @NotNull final Logger logger, @NotNull Function<JarFile, ExpansionDescription> descriptionFactory, @NotNull Function<ExpansionClassLoader, Expansion> expansionFactory, @NotNull final ClassLoader parentClassLoader) {
    this.logger = logger;
    this.addonsDir = addonsDir;
    this.descriptionFactory = descriptionFactory;
    this.expansionFactory = expansionFactory;
    this.parentClassLoader = parentClassLoader;
    if (!addonsDir.exists()) {
      if (addonsDir.mkdirs()) {
        logger.finest("Created addon directory");
      } else {
        throw new IllegalStateException("Cannot create addon directory");
      }
    }
  }

  /**
   * Create a new addon manager
   *
   * @param addonsDir          the directory to store addon files
   * @param logger             the logger to use in every addon
   * @param descriptionFactory the loader to load addon description
   * @param expansionFactory
   */
  public ExpansionManager(@NotNull final File addonsDir, @NotNull final Logger logger, @NotNull Function<JarFile, ExpansionDescription> descriptionFactory, @NotNull Function<ExpansionClassLoader, Expansion> expansionFactory) {
    this(addonsDir, logger, descriptionFactory, expansionFactory, ExpansionManager.class.getClassLoader());
  }

  public ExpansionManager(@NotNull final File addonsDir, @NotNull final Logger logger, @NotNull Function<JarFile, ExpansionDescription> descriptionFactory, @NotNull final ClassLoader parentClassLoader) {
    this(addonsDir, logger, descriptionFactory, DEFAULT_EXTENSION_FACTORY, parentClassLoader);
  }

  public ExpansionManager(@NotNull final File addonsDir, @NotNull final Logger logger, @NotNull Function<JarFile, ExpansionDescription> descriptionFactory) {
    this(addonsDir, logger, descriptionFactory, ExpansionManager.class.getClassLoader());
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
  public Function<JarFile, ExpansionDescription> getDescriptionFactory() {
    return descriptionFactory;
  }

  @NotNull
  public Function<ExpansionClassLoader, Expansion> getExpansionFactory() {
    return expansionFactory;
  }

  public ClassLoader getParentClassLoader() {
    return parentClassLoader;
  }

  public void addStateListener(@NotNull ExpansionStateListener listener) {
    this.stateListeners.add(listener);
  }

  public void removeStateListener(@NotNull ExpansionStateListener listener) {
    this.stateListeners.remove(listener);
  }

  /**
   * Load all addons from the addon directory. Also call {@link Expansion#onLoad()}
   */
  public void loadAddons() {
    final Map<String, ExpansionClassLoader> initClassLoaders = new HashMap<>();

    // Load the addon files
    Arrays.stream(Objects.requireNonNull(this.addonsDir.listFiles()))
      .filter(file -> file.isFile() && file.getName().toLowerCase(Locale.ROOT).endsWith(".jar"))
      .forEach(file -> {
        ExpansionDescription addonDescription;
        ExpansionClassLoader loader;
        try (final JarFile jar = new JarFile(file)) {
          // Get addon description
          addonDescription = descriptionFactory.apply(jar);
          if (initClassLoaders.containsKey(addonDescription.getName())) {
            this.logger.warning("Duplicated addon " + addonDescription.getName());
            return;
          }
          loader = new ExpansionClassLoader(this, file, addonDescription, this.parentClassLoader);
        } catch (final Exception e) {
          this.logger.log(Level.WARNING, e, () -> "Error when loading file " + file.getName());
          return;
        }

        try {
          loader.setState(ExpansionState.LOADING);
        } catch (final Throwable t) {
          this.logger.log(Level.WARNING, t, () -> "Error when loading file " + file.getName());
          loader.setState(ExpansionState.ERROR);
        }

        initClassLoaders.put(addonDescription.getName(), loader);
      });

    // Filter and sort the addons
    final Map<String, ExpansionClassLoader> loadingClassLoaders = initClassLoaders.entrySet().stream()
      .filter(entry -> entry.getValue().getState() == ExpansionState.LOADING)
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    final Map<String, ExpansionClassLoader> sortedClassLoaders = this.sortAndFilter(loadingClassLoaders);
    final Map<String, ExpansionClassLoader> remainingClassLoaders = initClassLoaders.entrySet().stream()
      .filter(entry -> !sortedClassLoaders.containsKey(entry.getKey()))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    remainingClassLoaders.values().forEach(loader -> loader.setState(ExpansionState.ERROR));

    // Store the final addons map
    this.classLoaders.putAll(remainingClassLoaders);
    this.classLoaders.putAll(sortedClassLoaders);

    // Load the addons
    sortedClassLoaders.forEach((key, loader) -> {
      try {
        final Expansion addon = loader.getAddon();
        if (!addon.onLoad()) {
          throw new IllegalStateException("onLoad() returned false");
        }
        this.logger.info("Loaded " + key + " " + loader.getAddonDescription().getVersion());
        loader.setState(ExpansionState.LOADED);
      } catch (final Throwable t) {
        this.logger.log(Level.WARNING, t, () -> "Error when loading addon " + key);
        loader.setState(ExpansionState.ERROR);
      }
    });
  }

  /**
   * Filter and sort the order of the addons
   *
   * @param original the original map
   *
   * @return the sorted and filtered map
   */
  @NotNull
  protected Map<String, ExpansionClassLoader> sortAndFilter(@NotNull final Map<String, ExpansionClassLoader> original) {
    return original;
  }

  /**
   * Enable all addons from the addon directory
   */
  public void enableAddons() {
    this.classLoaders.forEach((name, loader) -> {
      if (loader.getState() == ExpansionState.LOADED) {
        Expansion addon = loader.getAddon();
        try {
          loader.setState(ExpansionState.ENABLING);
          addon.onEnable();
          loader.setState(ExpansionState.ENABLED);
          this.logger.log(Level.INFO, "Enabled {0} {1}", new Object[]{name, loader.getAddonDescription().getVersion()});
        } catch (final Throwable t) {
          this.logger.log(Level.WARNING, t, () -> "Error when enabling " + name);
          loader.setState(ExpansionState.ERROR);
        }
      }
    });
  }

  /**
   * Disable all enabled addons
   */
  public void disableAddons() {
    Deque<Map.Entry<String, ExpansionClassLoader>> stack = new LinkedList<>(this.classLoaders.entrySet());
    while (true) {
      final Map.Entry<String, ExpansionClassLoader> entry = stack.pollLast();
      if (entry == null) break;
      final String name = entry.getKey();
      final ExpansionClassLoader loader = entry.getValue();
      if (loader.getState() == ExpansionState.ENABLED) {
        try {
          loader.setState(ExpansionState.DISABLING);
          loader.getAddon().onDisable();
          loader.setState(ExpansionState.DISABLED);
          this.logger.log(Level.INFO, "Disabled {0} {1}", new Object[]{name, loader.getAddonDescription().getVersion()});
        } catch (final Throwable t) {
          this.logger.log(Level.WARNING, t, () -> "Error when disabling " + name);
          loader.setState(ExpansionState.ERROR);
        }
      }
    }
    this.classLoaders.values().forEach(this::closeClassLoaderSafe);
    this.classLoaders.clear();
  }

  /**
   * Call the consumer for all enabled addon
   *
   * @param consumer the consumer
   */
  public void call(Consumer<Expansion> consumer) {
    this.classLoaders.values().forEach(classLoader -> {
      if (classLoader.getState() == ExpansionState.ENABLED) {
        consumer.accept(classLoader.getAddon());
      }
    });
  }

  /**
   * Call the consumer for all enabled addon
   *
   * @param consumer the consumer
   */
  public <T> void call(Class<T> clazz, Consumer<T> consumer) {
    call(addon -> {
      if (clazz.isInstance(addon)) {
        consumer.accept(clazz.cast(addon));
      }
    });
  }

  /**
   * Get the enabled addon
   *
   * @param name the name of the addon
   *
   * @return the addon, or null if it's not found
   */
  @Nullable
  public Expansion getAddon(@NotNull final String name) {
    final ExpansionClassLoader classLoader = this.classLoaders.get(name);
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
    ExpansionClassLoader classLoader = this.classLoaders.get(name);
    return classLoader != null && classLoader.getState() == ExpansionState.ENABLED;
  }

  /**
   * Get all loaded addons
   *
   * @return the loaded addons
   */
  @NotNull
  public Map<String, Expansion> getLoadedAddons() {
    return this.classLoaders.entrySet()
      .parallelStream()
      .filter(entry -> entry.getValue().getState() == ExpansionState.ENABLED)
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
  Class<?> findClass(@NotNull final ExpansionClassLoader classLoader, @NotNull final String name) {
    return this.classLoaders.entrySet()
      .parallelStream()
      .filter(entry -> entry.getValue() != classLoader)
      .flatMap(entry -> Optional.ofNullable(entry.getValue().findClass(name, false)).map(Stream::of).orElse(Stream.empty()))
      .findAny()
      .orElse(null);
  }

  void notifyStateChange(@NotNull final ExpansionClassLoader classLoader, @NotNull final ExpansionState state) {
    for (final ExpansionStateListener listener : this.stateListeners) {
      listener.onStateChange(classLoader, state);
    }
  }

  /**
   * Close the class loader safely
   *
   * @param classLoader the class loader
   */
  private void closeClassLoaderSafe(@NotNull final ExpansionClassLoader classLoader) {
    try {
      classLoader.close();
    } catch (final IOException e) {
      this.logger.log(Level.WARNING, "Error when closing ClassLoader", e);
    }
  }
}
