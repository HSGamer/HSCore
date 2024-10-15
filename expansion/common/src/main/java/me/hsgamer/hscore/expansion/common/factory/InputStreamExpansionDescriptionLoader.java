package me.hsgamer.hscore.expansion.common.factory;

import me.hsgamer.hscore.expansion.common.ExpansionDescription;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The factory that loads the {@link ExpansionDescription} from the {@link InputStream} of the jar file
 */
public abstract class InputStreamExpansionDescriptionLoader implements MapExpansionDescriptionLoader {
  private final String descriptionFileName;

  /**
   * Create a new {@link InputStreamExpansionDescriptionLoader}
   *
   * @param descriptionFileName the name of the description file
   */
  protected InputStreamExpansionDescriptionLoader(String descriptionFileName) {
    this.descriptionFileName = descriptionFileName;
  }

  /**
   * Load the information map from the input stream
   *
   * @param inputStream the input stream
   *
   * @return the information map
   */
  public abstract Map<String, Object> applyAsMap(InputStream inputStream);

  @Override
  public Map<String, Object> applyAsMap(JarFile jarFile) {
    JarEntry jarEntry = jarFile.getJarEntry(descriptionFileName);
    if (jarEntry == null) {
      throw new IllegalStateException("The file " + jarFile.getName() + " does not contain the file " + descriptionFileName);
    }
    try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
      return applyAsMap(inputStream);
    } catch (IOException e) {
      throw new IllegalStateException("There is an I/O error when loading configuration of " + jarFile.getName(), e);
    }
  }
}
