package me.hsgamer.hscore.bukkit.addon;

import me.hsgamer.hscore.addon.loader.InputStreamAddonDescriptionLoader;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * The {@link me.hsgamer.hscore.addon.loader.AddonDescriptionLoader} for Bukkit
 */
public class BukkitConfigAddonDescriptionLoader extends InputStreamAddonDescriptionLoader {
  /**
   * Create a new {@link BukkitConfigAddonDescriptionLoader}
   *
   * @param addonFileName the addon file name
   */
  public BukkitConfigAddonDescriptionLoader(String addonFileName) {
    super(addonFileName);
  }

  /**
   * Create a new {@link BukkitConfigAddonDescriptionLoader}
   */
  public BukkitConfigAddonDescriptionLoader() {
    this("addon.yml");
  }

  @Override
  public Map<String, Object> loadAsMap(InputStream inputStream) throws IOException {
    YamlConfiguration yamlConfiguration = new YamlConfiguration();
    try {
      yamlConfiguration.load(new InputStreamReader(inputStream));
    } catch (InvalidConfigurationException e) {
      throw new IOException(e);
    }
    return yamlConfiguration.getValues(false);
  }
}
