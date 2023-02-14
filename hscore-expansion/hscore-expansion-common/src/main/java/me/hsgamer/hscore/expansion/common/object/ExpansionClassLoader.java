package me.hsgamer.hscore.expansion.common.object;

import me.hsgamer.hscore.expansion.common.manager.ExpansionManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * The class loader of the addon
 */
public final class ExpansionClassLoader<T extends Expansion> extends URLClassLoader {

  /**
   * The jar file of the addon
   */
  @NotNull
  private final File file;

  /**
   * The addon manager
   */
  @NotNull
  private final ExpansionManager<T> addonManager;

  /**
   * The addon's description
   */
  @NotNull
  private final ExpansionDescription addonDescription;

  /**
   * The addon
   */
  @Nullable
  private T addon;

  /**
   * Create an Addon Class Loader
   *
   * @param addonManager     the addon manager
   * @param file             the addon jar
   * @param addonDescription the description for the addon
   * @param parent           the parent class loader
   *
   * @throws MalformedURLException if it cannot convert the file to its related URL
   */
  public ExpansionClassLoader(@NotNull final ExpansionManager<T> addonManager, @NotNull final File file,
                              @NotNull final ExpansionDescription addonDescription,
                              @NotNull final ClassLoader parent)
    throws MalformedURLException {
    super(new URL[]{file.toURI().toURL()}, parent);
    this.addonManager = addonManager;
    this.file = file;
    this.addonDescription = addonDescription;
  }

  /**
   * Get the addon
   *
   * @return the addon
   */
  @NotNull
  public T getAddon() {
    if (this.addon == null) {
      this.addon = this.addonManager.getExpansionFactory().create(this);
    }
    return this.addon;
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
  public Class<?> findClass(@NotNull final String name, final boolean global) {
    Class<?> clazz = null;
    try {
      clazz = super.findClass(name);
    } catch (final ClassNotFoundException | NoClassDefFoundError e) {
      if (global) {
        clazz = this.addonManager.findClass(this, name);
      }
    }
    return clazz;
  }

  /**
   * Get the addon jar
   *
   * @return the addon jar
   */
  @NotNull
  public File getFile() {
    return this.file;
  }

  /**
   * Get the addon manager
   *
   * @return the addon manager
   */
  @NotNull
  public ExpansionManager<T> getAddonManager() {
    return this.addonManager;
  }

  /**
   * Get the addon's description
   *
   * @return the description
   */
  @NotNull
  public ExpansionDescription getAddonDescription() {
    return this.addonDescription;
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
}
