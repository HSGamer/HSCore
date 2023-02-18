package me.hsgamer.hscore.addon.object;

import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.expansion.common.ExpansionClassLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * The main class of the addon
 */
public abstract class Addon implements Expansion {
  private final ExpansionClassLoader expansionClassLoader;
  private final AddonDescription description;
  private final File dataFolder;
  private final Logger logger;

  protected Addon() {
    ClassLoader classLoader = getClass().getClassLoader();
    if (!(classLoader instanceof ExpansionClassLoader)) {
      throw new IllegalStateException("Cannot create an addon without AddonClassLoader");
    }
    this.expansionClassLoader = (ExpansionClassLoader) classLoader;
    this.description = new AddonDescription(expansionClassLoader.getDescription());
    this.dataFolder = new File(expansionClassLoader.getManager().getExpansionsDir(), description.getName());
    this.logger = Logger.getLogger(description.getName());
  }

  /**
   * Called after all addons enabled
   */
  public void onPostEnable() {
    // EMPTY
  }

  /**
   * Called when reloading
   */
  public void onReload() {
    // EMPTY
  }

  /**
   * Get the addon's description
   *
   * @return the description
   */
  @NotNull
  public final AddonDescription getDescription() {
    return description;
  }

  /**
   * Get the addon's folder
   *
   * @return the directory for the addon
   */
  @NotNull
  public final File getDataFolder() {
    if (!this.dataFolder.exists()) {
      this.dataFolder.mkdirs();
    }
    return this.dataFolder;
  }

  /**
   * Copy the resource from the addon's jar
   *
   * @param path    path to resource
   * @param replace whether it replaces the existed one
   */
  public final void saveResource(@NotNull final String path, final boolean replace) {
    if (path.isEmpty()) {
      throw new IllegalArgumentException("Path cannot be null or empty");
    }
    final String newPath = path.replace('\\', '/');
    try (final JarFile jar = new JarFile(this.expansionClassLoader.getFile())) {
      final JarEntry jarConfig = jar.getJarEntry(newPath);
      if (jarConfig != null) {
        try (final InputStream in = jar.getInputStream(jarConfig)) {
          if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + newPath + "' cannot be found");
          }
          final File out = new File(this.getDataFolder(), newPath);
          out.getParentFile().mkdirs();
          if (!out.exists() || replace) {
            Files.copy(in, out.toPath(), StandardCopyOption.REPLACE_EXISTING);
          }
        }
      } else {
        throw new IllegalArgumentException("The embedded resource '" + newPath + "' cannot be found");
      }
    } catch (final IOException e) {
      logger.warning("Could not load from jar file. " + newPath);
    }
  }

  /**
   * Get the resource from the addon's jar
   *
   * @param path path to resource
   *
   * @return the InputStream of the resource, or null if it's not found
   */
  @Nullable
  public final InputStream getResource(@NotNull final String path) {
    if (path.isEmpty()) {
      throw new IllegalArgumentException("Path cannot be null or empty");
    }
    final String newPath = path.replace('\\', '/');
    try (final JarFile jar = new JarFile(this.expansionClassLoader.getFile())) {
      final JarEntry jarConfig = jar.getJarEntry(newPath);
      if (jarConfig != null) {
        try (final InputStream in = jar.getInputStream(jarConfig)) {
          return in;
        }
      }
    } catch (final IOException e) {
      logger.warning("Could not load from jar file. " + newPath);
    }
    return null;
  }

  /**
   * Get the expansion class loader
   *
   * @return the class loader
   */
  public ExpansionClassLoader getExpansionClassLoader() {
    return expansionClassLoader;
  }
}
