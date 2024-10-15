package me.hsgamer.hscore.expansion.extra.expansion;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * An interface for {@link me.hsgamer.hscore.expansion.common.Expansion} to make it easier to get the data folder
 */
public interface DataFolder extends GetClassLoader {
  static String normalizeJarPath(String path) {
    return path.replace('\\', '/');
  }

  /**
   * Get the data folder of the expansion
   *
   * @return the data folder
   */
  @NotNull
  default File getDataFolder() {
    return new File(this.getExpansionClassLoader().getManager().getExpansionsDir(), this.getExpansionClassLoader().getDescription().getName());
  }

  /**
   * Get the resource from the expansion jar
   *
   * @param path path to resource
   *
   * @return the InputStream of the resource, or null if it's not found
   */
  @Nullable
  default InputStream getResource(@NotNull final String path) {
    if (path.isEmpty()) {
      throw new IllegalArgumentException("Path cannot be null or empty");
    }
    final String newPath = normalizeJarPath(path);
    try (final JarFile jar = new JarFile(this.getExpansionClassLoader().getFile())) {
      final JarEntry jarConfig = jar.getJarEntry(newPath);
      if (jarConfig != null) {
        return jar.getInputStream(jarConfig);
      }
    } catch (final IOException e) {
      throw new IllegalArgumentException("Cannot load path " + newPath, e);
    }
    return null;
  }

  /**
   * Copy the resource from the expansion jar
   *
   * @param path    path to resource
   * @param replace whether it replaces the existed one
   */
  default void saveResource(@NotNull final String path, final boolean replace) {
    try (InputStream inputStream = this.getResource(path)) {
      if (inputStream == null) {
        throw new IllegalArgumentException("The embedded resource '" + path + "' cannot be found");
      }
      final File out = new File(this.getDataFolder(), normalizeJarPath(path));
      final File parent = out.getParentFile();
      if (parent != null && !parent.exists() && !parent.mkdirs()) {
        throw new IllegalStateException("Cannot create parent folder");
      }
      if (!out.exists() || replace) {
        Files.copy(inputStream, out.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (final IOException e) {
      throw new IllegalArgumentException("Cannot load path " + path, e);
    }
  }
}
