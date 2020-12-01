package me.hsgamer.hscore.addon.object;

import me.hsgamer.hscore.addon.AddonManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * The class loader of the addon
 */
public final class AddonClassLoader extends URLClassLoader {

  /**
   * The addon
   */
  @NotNull
  private final Addon addon;

  /**
   * The jar file of the addon
   */
  @NotNull
  private final File file;

  /**
   * The addon manager
   */
  @NotNull
  private final AddonManager addonManager;

  /**
   * The addon's description
   */
  @NotNull
  private final AddonDescription addonDescription;

  /**
   * Create an Addon Class Loader
   *
   * @param addonManager     the addon manager
   * @param file             the addon jar
   * @param addonDescription the description for the addon
   * @param parent           the parent class loader
   *
   * @throws MalformedURLException     if it cannot convert the file to its related URL
   * @throws IllegalAccessException    if it cannot create an instance of the main class of the
   *                                   addon
   * @throws InvocationTargetException if the constructor throws an exception
   * @throws InstantiationException    if the main class is an abstract class
   * @throws NoSuchMethodException     if it cannot find the constructor
   * @throws ClassNotFoundException    if the main class is not found
   */
  public AddonClassLoader(@NotNull final AddonManager addonManager, @NotNull final File file,
                          @NotNull final AddonDescription addonDescription,
                          @NotNull final ClassLoader parent)
    throws MalformedURLException, IllegalAccessException, InvocationTargetException, InstantiationException,
    NoSuchMethodException, ClassNotFoundException {
    super(new URL[]{file.toURI().toURL()}, parent);
    this.addonManager = addonManager;
    this.file = file;
    final Class<?> clazz = Class.forName(addonDescription.getMainClass(), true, this);
    final Class<? extends Addon> newClass;
    if (Addon.class.isAssignableFrom(clazz)) {
      newClass = clazz.asSubclass(Addon.class);
    } else {
      throw new ClassCastException("The main class does not extend Addon");
    }
    this.addon = newClass.getDeclaredConstructor().newInstance();
    this.addonDescription = addonDescription;
  }

  /**
   * Get the addon
   *
   * @return the addon
   */
  @NotNull
  public Addon getAddon() {
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
        clazz = this.addonManager.findClass(this.addon, name);
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
  public AddonManager getAddonManager() {
    return this.addonManager;
  }

  /**
   * Get the addon's description
   *
   * @return the description
   */
  @NotNull
  public AddonDescription getAddonDescription() {
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
