package me.hsgamer.hscore.bukkit.expansion;

import me.hsgamer.hscore.expansion.common.factory.InputStreamExpansionDescriptionLoader;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * The factory to load {@link me.hsgamer.hscore.expansion.common.ExpansionDescription} from a Bukkit YAML file
 */
public class BukkitConfigExpansionDescriptionLoader extends InputStreamExpansionDescriptionLoader {
  /**
   * Create a new {@link BukkitConfigExpansionDescriptionLoader}
   *
   * @param descriptionFileName the name of the description file
   */
  public BukkitConfigExpansionDescriptionLoader(String descriptionFileName) {
    super(descriptionFileName);
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
