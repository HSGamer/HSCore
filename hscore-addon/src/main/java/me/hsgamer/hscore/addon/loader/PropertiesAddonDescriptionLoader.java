package me.hsgamer.hscore.addon.loader;

import me.hsgamer.hscore.addon.object.AddonDescription;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * The {@link AddonDescriptionLoader} that loads the {@link AddonDescription} from the {@link Properties} file of the jar file
 */
public class PropertiesAddonDescriptionLoader extends InputStreamAddonDescriptionLoader {
  /**
   * Create a new {@link PropertiesAddonDescriptionLoader}
   *
   * @param addonFileName the addon file name
   */
  public PropertiesAddonDescriptionLoader(String addonFileName) {
    super(addonFileName);
  }

  /**
   * Create a new {@link PropertiesAddonDescriptionLoader}
   */
  public PropertiesAddonDescriptionLoader() {
    this("addon.properties");
  }

  @Override
  public Map<String, Object> loadAsMap(InputStream inputStream) throws IOException {
    Properties properties = new Properties();
    properties.load(inputStream);
    return properties.entrySet().stream().collect(Collectors.toMap(entry -> Objects.toString(entry.getKey()), Map.Entry::getValue));
  }
}
