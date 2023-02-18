package me.hsgamer.hscore.expansion.manifest;

import me.hsgamer.hscore.expansion.common.ExpansionDescription;
import me.hsgamer.hscore.expansion.common.exception.InvalidExpansionDescriptionException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * The factory that loads the {@link ExpansionDescription} from the manifest of the jar file
 */
public class ManifestExpansionDescriptionLoader implements Function<JarFile, ExpansionDescription> {
  @Override
  public ExpansionDescription apply(JarFile jarFile) {
    Manifest manifest;
    try {
      manifest = jarFile.getManifest();
    } catch (IOException e) {
      throw new IllegalStateException("Cannot get the manifest file of the file " + jarFile.getName(), e);
    }
    if (manifest == null) {
      throw new InvalidExpansionDescriptionException(jarFile.getName() + " does not have a manifest");
    }
    Attributes attributes = manifest.getMainAttributes();
    String name = attributes.getValue("Implementation-Title");
    if (name == null) {
      throw new InvalidExpansionDescriptionException(jarFile.getName() + " does not have a Implementation-Title");
    }
    String version = attributes.getValue("Implementation-Version");
    if (version == null) {
      throw new InvalidExpansionDescriptionException(jarFile.getName() + " does not have a Implementation-Version");
    }
    String mainClass = attributes.getValue("Main-Class");
    if (mainClass == null) {
      throw new InvalidExpansionDescriptionException(jarFile.getName() + " does not have a Main-Class");
    }
    Map<String, Object> data = new HashMap<>();
    attributes.forEach((key, value) -> data.put(Objects.toString(key), value));
    return new ExpansionDescription(name, version, mainClass, data);
  }
}
