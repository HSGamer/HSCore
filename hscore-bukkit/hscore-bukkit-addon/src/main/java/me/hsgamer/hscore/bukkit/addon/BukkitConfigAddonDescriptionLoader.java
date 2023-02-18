package me.hsgamer.hscore.bukkit.addon;

import me.hsgamer.hscore.expansion.common.factory.InputStreamExpansionDescriptionLoader;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * The factory to load {@link me.hsgamer.hscore.addon.object.AddonDescription} from a Bukkit YAML file
 */
public class BukkitConfigAddonDescriptionLoader extends InputStreamExpansionDescriptionLoader {
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
  public Map<String, Object> applyAsMap(InputStream inputStream) {
    YamlConfiguration yamlConfiguration = new YamlConfiguration();
    try {
      yamlConfiguration.load(new InputStreamReader(inputStream));
    } catch (InvalidConfigurationException | IOException e) {
      throw new IllegalStateException("Failed to load description from input stream", e);
    }
    return yamlConfiguration.getValues(false);
  }
}
