package me.hsgamer.hscore.addon.loader;

import me.hsgamer.hscore.addon.exception.InvalidAddonDescription;
import me.hsgamer.hscore.addon.object.AddonDescription;

import java.io.IOException;
import java.util.jar.JarFile;

/**
 * The interface to load {@link AddonDescription} from jar file
 */
public interface AddonDescriptionLoader {
  /**
   * Load {@link AddonDescription} from jar file
   *
   * @param jarFile the jar file
   *
   * @return the {@link AddonDescription}
   *
   * @throws IOException             if an I/O error occurs
   * @throws InvalidAddonDescription if the {@link AddonDescription} is invalid
   */
  AddonDescription load(JarFile jarFile) throws IOException, InvalidAddonDescription;
}
