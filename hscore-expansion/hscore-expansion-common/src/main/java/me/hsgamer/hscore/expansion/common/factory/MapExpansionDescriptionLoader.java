package me.hsgamer.hscore.expansion.common.factory;

import me.hsgamer.hscore.expansion.common.ExpansionDescription;
import me.hsgamer.hscore.expansion.common.exception.InvalidExpansionDescriptionException;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.jar.JarFile;

/**
 * The factory that loads the {@link ExpansionDescription} from the information map of the jar file.
 * The map should have:
 * <ul>
 *   <li>name: the name of the expansion</li>
 *   <li>version: the version of the expansion</li>
 *   <li>main: the main class of the expansion</li>
 * </ul>
 */
public interface MapExpansionDescriptionLoader extends Function<JarFile, ExpansionDescription> {
  /**
   * Load the information map from the jar file
   *
   * @param jarFile the jar file
   *
   * @return the information map
   */
  Map<String, Object> applyAsMap(JarFile jarFile);

  @Override
  default ExpansionDescription apply(JarFile jarFile) {
    Map<String, Object> data = applyAsMap(jarFile);
    final String name = Objects.toString(data.get("name"), null);
    final String version = Objects.toString(data.get("version"), null);
    final String mainClass = Objects.toString(data.get("main"), null);
    if (name == null) {
      throw new InvalidExpansionDescriptionException(jarFile.getName() + " doesn't define a name");
    }
    if (version == null) {
      throw new InvalidExpansionDescriptionException(jarFile.getName() + " doesn't define a version");
    }
    if (mainClass == null) {
      throw new IllegalArgumentException(jarFile.getName() + " doesn't define a main class");
    }
    return new ExpansionDescription(name, version, mainClass, data);
  }
}
