package me.hsgamer.hscore.addon.loader;

import me.hsgamer.hscore.addon.exception.InvalidAddonDescription;
import me.hsgamer.hscore.addon.object.AddonDescription;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The simplified {@link AddonDescriptionLoader} that loads the {@link AddonDescription} from the {@link InputStream} of the jar file
 */
public abstract class InputStreamAddonDescriptionLoader implements MapAddonDescriptionLoader {
  private final String addonFileName;

  /**
   * Create a new {@link InputStreamAddonDescriptionLoader}
   *
   * @param addonFileName the addon file name
   */
  protected InputStreamAddonDescriptionLoader(String addonFileName) {
    this.addonFileName = addonFileName;
  }

  /**
   * Load the information map from the input stream
   *
   * @param inputStream the input stream
   *
   * @return the information map
   *
   * @throws IOException if an I/O error occurs
   */
  public abstract Map<String, Object> loadAsMap(InputStream inputStream) throws IOException, InvalidAddonDescription;

  @Override
  public Map<String, Object> loadAsMap(JarFile jarFile) throws IOException, InvalidAddonDescription {
    JarEntry jarEntry = jarFile.getJarEntry(addonFileName);
    if (jarEntry == null) {
      throw new IOException("The file " + jarFile.getName() + " does not contain the file " + addonFileName);
    }
    InputStream inputStream = jarFile.getInputStream(jarEntry);
    try {
      return loadAsMap(inputStream);
    } catch (IOException e) {
      throw new IOException("There is an I/O error when loading configuration of " + jarFile.getName(), e);
    } catch (InvalidAddonDescription e) {
      throw new InvalidAddonDescription("The configuration of " + jarFile.getName() + " is invalid", e);
    }
  }
}
