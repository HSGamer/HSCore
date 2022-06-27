package me.hsgamer.hscore.bukkit.addon;

import me.hsgamer.hscore.addon.loader.MapAddonDescriptionLoader;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The {@link me.hsgamer.hscore.addon.loader.AddonDescriptionLoader} for Bukkit
 */
public class BukkitConfigAddonDescriptionLoader implements MapAddonDescriptionLoader {
  private final String addonFileName;

  /**
   * Create a new {@link BukkitConfigAddonDescriptionLoader}
   *
   * @param addonFileName the addon file name
   */
  public BukkitConfigAddonDescriptionLoader(String addonFileName) {
    this.addonFileName = addonFileName;
  }

  /**
   * Create a new {@link BukkitConfigAddonDescriptionLoader}
   */
  public BukkitConfigAddonDescriptionLoader() {
    this("addon.yml");
  }

  @Override
  public Map<String, Object> loadAsMap(JarFile jarFile) throws IOException {
    JarEntry jarEntry = jarFile.getJarEntry(addonFileName);
    if (jarEntry == null) {
      throw new IOException("The file " + jarFile.getName() + " does not contain the file " + addonFileName);
    }
    InputStream inputStream = jarFile.getInputStream(jarEntry);
    YamlConfiguration yamlConfiguration = new YamlConfiguration();
    try {
      yamlConfiguration.load(new InputStreamReader(inputStream));
    } catch (InvalidConfigurationException e) {
      throw new IOException("The file " + jarFile.getName() + " contains an invalid configuration", e);
    }
    return yamlConfiguration.getValues(false);
  }
}
