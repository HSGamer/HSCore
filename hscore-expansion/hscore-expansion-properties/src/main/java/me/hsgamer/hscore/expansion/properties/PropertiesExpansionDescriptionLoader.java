package me.hsgamer.hscore.expansion.properties;

import me.hsgamer.hscore.expansion.common.factory.InputStreamExpansionDescriptionLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * The factory that loads the {@link me.hsgamer.hscore.expansion.common.ExpansionDescription} from the {@link Properties} file of the jar file
 */
public class PropertiesExpansionDescriptionLoader extends InputStreamExpansionDescriptionLoader {

  /**
   * Create a new {@link PropertiesExpansionDescriptionLoader}
   *
   * @param descriptionFileName the name of the description file
   */
  public PropertiesExpansionDescriptionLoader(String descriptionFileName) {
    super(descriptionFileName);
  }

  /**
   * Create a new {@link PropertiesExpansionDescriptionLoader} with the default description file name
   */
  public PropertiesExpansionDescriptionLoader() {
    this("expansion.properties");
  }

  @Override
  public Map<String, Object> applyAsMap(InputStream inputStream) {
    Properties properties = new Properties();
    try {
      properties.load(inputStream);
    } catch (IOException e) {
      throw new IllegalStateException("Cannot load the expansion description", e);
    }
    return properties.entrySet().stream().collect(Collectors.toMap(entry -> Objects.toString(entry.getKey()), Map.Entry::getValue));
  }
}
