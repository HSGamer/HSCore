package me.hsgamer.hscore.expansion.common;

import java.io.IOException;
import java.util.jar.JarFile;

/**
 * The interface to load {@link ExpansionDescription} from jar file
 */
public interface ExpansionDescriptionLoader {
  /**
   * Load {@link ExpansionDescription} from jar file
   *
   * @param jarFile the jar file
   *
   * @return the {@link ExpansionDescription}
   *
   * @throws IOException               if an I/O error occurs
   * @throws InvalidExpansionException if the {@link ExpansionDescription} is invalid
   */
  ExpansionDescription load(JarFile jarFile) throws IOException, InvalidExpansionException;
}
