package me.hsgamer.hscore.expansion.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

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
  @NotNull
  private final Expansion expansion;

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
    this.expansion = this.manager.getExpansionFactory().apply(this);
  }

  /**
   * Get the expansion
   *
   * @return the expansion
   */
  @NotNull
  public Expansion getExpansion() {
    return this.expansion;
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
  void setThrowable(@NotNull final Throwable throwable) {
    this.throwable = throwable;
  }

  @Override
  @NotNull
  protected Class<?> findClass(@NotNull final String name) throws ClassNotFoundException {
    Class<?> clazz = this.findClass(name, true);
    if (clazz == null) {
      throw new ClassNotFoundException(name);
    } else {
      return clazz;
    }
  }

  /**
   * Get class by the name
   *
   * @param name   the class name
   * @param global whether it'll try to search globally
   *
   * @return the class, or null if it's not found
   */
  @Nullable
  Class<?> findClass(@NotNull final String name, final boolean global) {
    Class<?> clazz = null;
    try {
      clazz = super.findClass(name);
    } catch (final ClassNotFoundException | NoClassDefFoundError e) {
      if (global) {
        clazz = this.manager.findClass(this, name);
      }
    }
    return clazz;
  }
}
