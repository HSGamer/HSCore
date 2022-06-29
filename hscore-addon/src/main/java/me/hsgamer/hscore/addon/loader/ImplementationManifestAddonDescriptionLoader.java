package me.hsgamer.hscore.addon.loader;

import me.hsgamer.hscore.addon.exception.InvalidAddonDescription;
import me.hsgamer.hscore.addon.object.AddonDescription;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * The {@link AddonDescriptionLoader} that loads the {@link AddonDescription} from the manifest of the jar file
 */
public class ImplementationManifestAddonDescriptionLoader implements AddonDescriptionLoader {
  @Override
  public AddonDescription load(JarFile jarFile) throws IOException, InvalidAddonDescription {
    Manifest manifest = jarFile.getManifest();
    if (manifest == null) {
      throw new InvalidAddonDescription(jarFile.getName() + " does not have a manifest");
    }
    Attributes attributes = manifest.getMainAttributes();
    String name = attributes.getValue("Implementation-Title");
    if (name == null) {
      throw new InvalidAddonDescription(jarFile.getName() + " does not have a Implementation-Title");
    }
    String version = attributes.getValue("Implementation-Version");
    if (version == null) {
      throw new InvalidAddonDescription(jarFile.getName() + " does not have a Implementation-Version");
    }
    String mainClass = attributes.getValue("Main-Class");
    if (mainClass == null) {
      throw new InvalidAddonDescription(jarFile.getName() + " does not have a Main-Class");
    }
    Map<String, Object> data = new HashMap<>();
    attributes.forEach((key, value) -> data.put(Objects.toString(key), value));
    return new AddonDescription(name, version, mainClass, data);
  }
}
