package me.hsgamer.hscore.expansion.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class that manages all {@link Expansion}s
 */
public class ExpansionManager {
  /**
   * The default factory to create a new instance of the main class using the no-args constructor
   */
  public static final Function<ExpansionClassLoader, Expansion> DEFAULT_EXPANSION_FACTORY = classLoader -> {
    try {
      final Class<?> clazz = Class.forName(classLoader.getDescription().getMainClass(), true, classLoader);
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
   * The class loader map keyed expansion's id, valued expansion's class loader
   */
  protected final Map<String, ExpansionClassLoader> classLoaders = new LinkedHashMap<>();

  /**
   * The directory that contains all expansions
   */
  @NotNull
  private final File expansionsDir;

  /**
   * The logger to use in all expansions
   */
  @NotNull
  private final Logger logger;

  /**
   * The parent class loader to load all expansions
   */
  @NotNull
  private final ClassLoader parentClassLoader;

  /**
   * The factory to create {@link ExpansionDescription} from {@link JarFile}
   */
  @NotNull
  private final Function<JarFile, ExpansionDescription> descriptionFactory;

  /**
   * The factory to create {@link Expansion} from {@link ExpansionClassLoader}
   */
  @NotNull
  private final Function<ExpansionClassLoader, Expansion> expansionFactory;

  /**
   * The set of listeners to listen to the state of the expansion
   */
  @NotNull
  private final Set<BiConsumer<ExpansionClassLoader, ExpansionState>> stateListeners = new HashSet<>();

  /**
   * Create a new expansion manager
   *
   * @param expansionsDir      the directory to store expansion files
   * @param logger             the logger to use in every expansion
   * @param descriptionFactory the factory to load description
   * @param expansionFactory   the factory to load expansion instance
   * @param parentClassLoader  the parent class loader to load all expansions
   */
  public ExpansionManager(@NotNull final File expansionsDir, @NotNull final Logger logger, @NotNull Function<JarFile, ExpansionDescription> descriptionFactory, @NotNull Function<ExpansionClassLoader, Expansion> expansionFactory, @NotNull final ClassLoader parentClassLoader) {
    this.logger = logger;
    this.expansionsDir = expansionsDir;
    this.descriptionFactory = descriptionFactory;
    this.expansionFactory = expansionFactory;
    this.parentClassLoader = parentClassLoader;
    if (!expansionsDir.exists()) {
      if (expansionsDir.mkdirs()) {
        logger.finest("Created expansion directory");
      } else {
        throw new IllegalStateException("Cannot create expansion directory");
      }
    }
  }


  /**
   * Create a new expansion manager
   *
   * @param expansionsDir      the directory to store expansion files
   * @param logger             the logger to use in every expansion
   * @param descriptionFactory the factory to load description
   * @param expansionFactory   the factory to load expansion instance
   */
  public ExpansionManager(@NotNull final File expansionsDir, @NotNull final Logger logger, @NotNull Function<JarFile, ExpansionDescription> descriptionFactory, @NotNull Function<ExpansionClassLoader, Expansion> expansionFactory) {
    this(expansionsDir, logger, descriptionFactory, expansionFactory, ExpansionManager.class.getClassLoader());
  }

  /**
   * Create a new expansion manager
   *
   * @param expansionsDir      the directory to store expansion files
   * @param logger             the logger to use in every expansion
   * @param descriptionFactory the factory to load description
   * @param parentClassLoader  the parent class loader to load all expansions
   */
  public ExpansionManager(@NotNull final File expansionsDir, @NotNull final Logger logger, @NotNull Function<JarFile, ExpansionDescription> descriptionFactory, @NotNull final ClassLoader parentClassLoader) {
    this(expansionsDir, logger, descriptionFactory, DEFAULT_EXPANSION_FACTORY, parentClassLoader);
  }

  /**
   * Create a new expansion manager
   *
   * @param expansionsDir      the directory to store expansion files
   * @param logger             the logger to use in every expansion
   * @param descriptionFactory the factory to load description
   */
  public ExpansionManager(@NotNull final File expansionsDir, @NotNull final Logger logger, @NotNull Function<JarFile, ExpansionDescription> descriptionFactory) {
    this(expansionsDir, logger, descriptionFactory, ExpansionManager.class.getClassLoader());
  }

  /**
   * Get the expansion directory
   *
   * @return the directory
   */
  @NotNull
  public final File getExpansionsDir() {
    return this.expansionsDir;
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
   * Get the description factory
   *
   * @return the factory
   */
  @NotNull
  public Function<JarFile, ExpansionDescription> getDescriptionFactory() {
    return descriptionFactory;
  }

  /**
   * Get the expansion factory
   *
   * @return the factory
   */
  @NotNull
  public Function<ExpansionClassLoader, Expansion> getExpansionFactory() {
    return expansionFactory;
  }

  /**
   * Get the parent class loader
   *
   * @return the parent class loader
   */
  @NotNull
  public ClassLoader getParentClassLoader() {
    return parentClassLoader;
  }

  /**
   * Add a new state listener
   *
   * @param listener the listener
   */
  public void addStateListener(@NotNull BiConsumer<ExpansionClassLoader, ExpansionState> listener) {
    this.stateListeners.add(listener);
  }

  /**
   * Remove a state listener
   *
   * @param listener the listener
   */
  public void removeStateListener(@NotNull BiConsumer<ExpansionClassLoader, ExpansionState> listener) {
    this.stateListeners.remove(listener);
  }

  /**
   * Get all loaded expansion class loaders
   *
   * @return the map of expansion class loaders
   */
  public Map<String, ExpansionClassLoader> getClassLoaders() {
    return Collections.unmodifiableMap(classLoaders);
  }

  /**
   * Load all expansions from the expansion directory. Also call {@link Expansion#onLoad()}
   */
  public void loadExpansions() {
    final Map<String, ExpansionClassLoader> initClassLoaders = new HashMap<>();

    // Load the expansion files
    Arrays.stream(Objects.requireNonNull(this.expansionsDir.listFiles()))
      .filter(file -> file.isFile() && file.getName().toLowerCase(Locale.ROOT).endsWith(".jar"))
      .forEach(file -> {
        ExpansionDescription description;
        ExpansionClassLoader loader;
        try (final JarFile jar = new JarFile(file)) {
          description = descriptionFactory.apply(jar);
          if (initClassLoaders.containsKey(description.getName())) {
            this.logger.warning("Duplicated expansion " + description.getName());
            return;
          }

          loader = new ExpansionClassLoader(this, file, description, this.parentClassLoader);
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

        initClassLoaders.put(description.getName(), loader);
      });

    // Filter and sort the loaders
    final Map<String, ExpansionClassLoader> loadingClassLoaders = initClassLoaders.entrySet().stream()
      .filter(entry -> entry.getValue().getState() == ExpansionState.LOADING)
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    final Map<String, ExpansionClassLoader> sortedClassLoaders = this.sortAndFilter(loadingClassLoaders);
    final Map<String, ExpansionClassLoader> remainingClassLoaders = initClassLoaders.entrySet().stream()
      .filter(entry -> !sortedClassLoaders.containsKey(entry.getKey()))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    remainingClassLoaders.values().forEach(loader -> loader.setState(ExpansionState.ERROR));

    // Store the final loader map
    this.classLoaders.putAll(remainingClassLoaders);
    this.classLoaders.putAll(sortedClassLoaders);

    // Load the expansions
    sortedClassLoaders.forEach((key, loader) -> {
      try {
        final Expansion expansion = loader.getExpansion();
        if (!expansion.onLoad()) {
          throw new IllegalStateException("onLoad() returned false");
        }
        this.logger.info("Loaded " + key + " " + loader.getDescription().getVersion());
        loader.setState(ExpansionState.LOADED);
      } catch (final Throwable t) {
        this.logger.log(Level.WARNING, t, () -> "Error when loading expansion " + key);
        loader.setState(ExpansionState.ERROR);
      }
    });
  }

  /**
   * Filter and sort the order of the expansion class loaders
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
   * Enable all loaded expansions
   */
  public void enableExpansions() {
    this.classLoaders.forEach((name, loader) -> {
      if (loader.getState() == ExpansionState.LOADED) {
        Expansion expansion = loader.getExpansion();
        try {
          loader.setState(ExpansionState.ENABLING);
          expansion.onEnable();
          loader.setState(ExpansionState.ENABLED);
          this.logger.log(Level.INFO, "Enabled {0} {1}", new Object[]{name, loader.getDescription().getVersion()});
        } catch (final Throwable t) {
          this.logger.log(Level.WARNING, t, () -> "Error when enabling " + name);
          loader.setState(ExpansionState.ERROR);
        }
      }
    });
  }

  /**
   * Disable all enabled expansions
   */
  public void disableExpansions() {
    Deque<Map.Entry<String, ExpansionClassLoader>> stack = new LinkedList<>(this.classLoaders.entrySet());
    while (true) {
      final Map.Entry<String, ExpansionClassLoader> entry = stack.pollLast();
      if (entry == null) break;
      final String name = entry.getKey();
      final ExpansionClassLoader loader = entry.getValue();
      if (loader.getState() == ExpansionState.ENABLED) {
        try {
          loader.setState(ExpansionState.DISABLING);
          loader.getExpansion().onDisable();
          loader.setState(ExpansionState.DISABLED);
          this.logger.log(Level.INFO, "Disabled {0} {1}", new Object[]{name, loader.getDescription().getVersion()});
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
   * Call the consumer for all enabled expansions
   *
   * @param consumer the consumer
   */
  public void call(Consumer<Expansion> consumer) {
    this.classLoaders.values().forEach(classLoader -> {
      if (classLoader.getState() == ExpansionState.ENABLED) {
        consumer.accept(classLoader.getExpansion());
      }
    });
  }

  /**
   * Call the consumer for all enabled expansions that match the specific class
   *
   * @param clazz    the class to limit what expansion can be called
   * @param consumer the consumer to call the cast expansion
   * @param <T>      the type of the class
   */
  public <T> void call(Class<T> clazz, Consumer<T> consumer) {
    call(expansion -> {
      if (clazz.isInstance(expansion)) {
        consumer.accept(clazz.cast(expansion));
      }
    });
  }

  /**
   * Get the loaded expansion class loader
   *
   * @param name the name of the expansion
   *
   * @return the expansion class loader
   */
  public Optional<ExpansionClassLoader> getExpansionClassLoader(@NotNull final String name) {
    return Optional.ofNullable(this.classLoaders.get(name));
  }

  /**
   * Get the loaded expansion
   *
   * @param name the name of the expansion
   *
   * @return the expansion
   */
  public Optional<Expansion> getExpansion(@NotNull final String name) {
    return getExpansionClassLoader(name).map(ExpansionClassLoader::getExpansion);
  }

  /**
   * Get all enabled expansions
   *
   * @return the enabled expansions
   */
  @NotNull
  public Map<String, Expansion> getEnabledExpansions() {
    return this.classLoaders.entrySet()
      .parallelStream()
      .filter(entry -> entry.getValue().getState() == ExpansionState.ENABLED)
      .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getExpansion()));
  }

  /**
   * Find a class for a class loader
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

  /**
   * Notify the state change to the listeners
   *
   * @param classLoader the extension class loader
   * @param state       the state
   */
  void notifyStateChange(@NotNull final ExpansionClassLoader classLoader, @NotNull final ExpansionState state) {
    for (final BiConsumer<ExpansionClassLoader, ExpansionState> listener : this.stateListeners) {
      listener.accept(classLoader, state);
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
