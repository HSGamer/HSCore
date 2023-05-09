package me.hsgamer.hscore.expansion.gson;

import com.google.gson.JsonElement;
import me.hsgamer.hscore.expansion.common.factory.InputStreamExpansionDescriptionLoader;
import me.hsgamer.hscore.gson.GsonUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * The factory that loads the {@link me.hsgamer.hscore.expansion.common.ExpansionDescription} from the json file of the jar file
 */
public class GsonExpansionDescriptionLoader extends InputStreamExpansionDescriptionLoader {
  /**
   * Create a new {@link GsonExpansionDescriptionLoader}
   *
   * @param descriptionFileName the name of the description file
   */
  public GsonExpansionDescriptionLoader(String descriptionFileName) {
    super(descriptionFileName);
  }

  /**
   * Create a new {@link GsonExpansionDescriptionLoader} with the default description file name
   */
  public GsonExpansionDescriptionLoader() {
    super("expansion.json");
  }

  @Override
  public Map<String, Object> applyAsMap(InputStream inputStream) {
    try (InputStreamReader reader = new InputStreamReader(inputStream)) {
      JsonElement element = GsonUtils.parse(reader);
      if (element.isJsonObject()) {
        Object object = GsonUtils.normalize(element.getAsJsonObject(), true);
        if (object instanceof Map) {
          //noinspection unchecked
          return (Map<String, Object>) object;
        }
      }
      throw new IllegalArgumentException("The description file must be a json object");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
