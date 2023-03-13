package me.hsgamer.hscore.expansion.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Optional;

/**
 * The class loader of the expansion
 */
public final class ExpansionClassLoader extends URLClassLoader {

  /**
   * The jar file of the expansion
   */
  @NotNull
  private final File file;

  /**
   * The expansion manager
   */
  @NotNull
  private final ExpansionManager manager;

  /**
   * The expansion's description
   */
  @NotNull
  private final ExpansionDescription description;

  /**
   * The expansion
   */
  @Nullable
  private Expansion expansion;

  /**
   * The state of the expansion
   */
  @NotNull
  private ExpansionState currentState = ExpansionState.UNKNOWN;

  /**
   * The throwable if the expansion is in {@link ExpansionState#ERROR}
   */
  private Throwable throwable;

  /**
   * Create a new class loader
   *
   * @param manager     the expansion manager
   * @param file        the expansion jar
   * @param description the description for the expansion
   * @param parent      the parent class loader
   *
   * @throws MalformedURLException if it cannot convert the file to its related URL
   */
  public ExpansionClassLoader(@NotNull final ExpansionManager manager, @NotNull final File file,
                              @NotNull final ExpansionDescription description,
                              @NotNull final ClassLoader parent)
    throws MalformedURLException {
    super(new URL[]{file.toURI().toURL()}, parent);
    this.manager = manager;
    this.file = file;
    this.description = description;
  }

  /**
   * Get the expansion if it's initialized
   *
   * @return the optional expansion
   */
  public Optional<Expansion> getExpansionOptional() {
    return Optional.ofNullable(this.expansion);
  }

  /**
   * Get the expansion
   *
   * @return the expansion
   *
   * @throws IllegalStateException if the expansion is not found or not initialized
   */
  @NotNull
  public Expansion getExpansion() {
    return this.getExpansionOptional().orElseThrow(() -> new IllegalStateException("Expansion not found or not initialized"));
  }

  /**
   * Get the expansion jar
   *
   * @return the expansion jar
   */
  @NotNull
  public File getFile() {
    return this.file;
  }

  /**
   * Get the expansion manager
   *
   * @return the expansion manager
   */
  @NotNull
  public ExpansionManager getManager() {
    return this.manager;
  }

  /**
   * Get the expansion's description
   *
   * @return the description
   */
  @NotNull
  public ExpansionDescription getDescription() {
    return this.description;
  }

  /**
   * Get the state of the expansion
   *
   * @return the state
   */
  @NotNull
  public ExpansionState getState() {
    return currentState;
  }

  /**
   * Set the state of the expansion
   *
   * @param state the state
   */
  void setState(@NotNull final ExpansionState state) {
    if (this.currentState == state) return;
    manager.notifyStateChange(this, state);
    this.currentState = state;
  }

  /**
   * Get the throwable if the expansion is in {@link ExpansionState#ERROR}
   *
   * @return the throwable
   */
  @Nullable
  public Throwable getThrowable() {
    return throwable;
  }

  /**
   * Set the throwable if the expansion is in {@link ExpansionState#ERROR}
   *
   * @param throwable the throwable
   */
  public void setThrowable(@NotNull final Throwable throwable) {
    this.throwable = throwable;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {
      Class<?> clazz = this.loadClass(name, resolve, true);
      if (clazz == null) {
        throw new ClassNotFoundException(name);
      }
      return clazz;
    }
  }

  /**
   * Load class by the name
   *
   * @param name    the class name
   * @param resolve whether it'll resolve the class
   * @param global  whether it'll try to search globally
   *
   * @return the class, or null if it's not found
   */
  @Nullable
  Class<?> loadClass(@NotNull final String name, final boolean resolve, final boolean global) {
    Class<?> clazz = null;
    try {
      clazz = super.loadClass(name, resolve);
    } catch (final ClassNotFoundException e) {
      if (global) {
        clazz = this.manager.loadClass(this, name, resolve);
      }
    }
    return clazz;
  }

  /**
   * Initialize the expansion
   */
  void initExpansion() {
    this.expansion = this.manager.getExpansionFactory().apply(this);
  }
}
