package me.hsgamer.hscore.addon.object;

import me.hsgamer.hscore.addon.AddonManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * The class loader of the addon
 */
public final class AddonClassLoader extends URLClassLoader {

  private final Addon addon;
  private final File file;
  private final AddonManager addonManager;

  /**
   * Create an Addon Class Loader
   *
   * @param addonManager     the addon manager
   * @param file             the addon jar
   * @param addonDescription the description for the addon
   * @param parent           the parent class loader
   * @throws MalformedURLException     if it cannot convert the file to its related URL
   * @throws IllegalAccessException    if it cannot create an instance of the main class of the
   *                                   addon
   * @throws InvocationTargetException if the constructor throws an exception
   * @throws InstantiationException    if the main class is an abstract class
   * @throws NoSuchMethodException     if it cannot find the constructor
   * @throws ClassNotFoundException    if the main class is not found
   */
  public AddonClassLoader(AddonManager addonManager, File file, AddonDescription addonDescription,
                          ClassLoader parent)
    throws MalformedURLException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException {
    super(new URL[]{file.toURI().toURL()}, parent);
    this.addonManager = addonManager;
    this.file = file;

    Class<?> clazz = Class.forName(addonDescription.getMainClass(), true, this);
    Class<? extends Addon> newClass;
    if (Addon.class.isAssignableFrom(clazz)) {
      newClass = clazz.asSubclass(Addon.class);
    } else {
      throw new ClassCastException("The main class does not extend Addon");
    }
    addon = newClass.getDeclaredConstructor().newInstance();
    addon.setDescription(addonDescription);
  }

  /**
   * Get the addon
   *
   * @return the addon
   */
  public Addon getAddon() {
    return addon;
  }

  @Override
  protected Class<?> findClass(String name) {
    return findClass(name, true);
  }

  /**
   * Get class by the name
   *
   * @param name   the class name
   * @param global whether it'll try to search globally
   * @return the class, or null if it's not found
   */
  public Class<?> findClass(String name, boolean global) {
    Class<?> clazz = null;
    if (global) {
      clazz = addonManager.findClass(addon, name);
    }
    if (clazz == null) {
      try {
        clazz = super.findClass(name);
      } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
        // IGNORED
      }
    }
    return clazz;
  }

  /**
   * Get the addon jar
   *
   * @return the addon jar
   */
  public File getFile() {
    return file;
  }

  /**
   * Get the addon manager
   *
   * @return the addon manager
   */
  public AddonManager getAddonManager() {
    return addonManager;
  }
}
