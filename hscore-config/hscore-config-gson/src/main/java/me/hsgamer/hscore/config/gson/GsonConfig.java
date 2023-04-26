package me.hsgamer.hscore.config.gson;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.logging.Level;

/**
 * The {@link Config} implementation for Gson
 */
public class GsonConfig implements Config {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private final File file;
  private JsonObject root = new JsonObject();

  /**
   * Create a new config
   *
   * @param file the file
   */
  public GsonConfig(File file) {
    this.file = file;
  }

  private static Map<PathString, Object> getValues(JsonObject object, boolean deep) {
    Map<PathString, Object> values = new HashMap<>();
    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
      PathString key = new PathString(entry.getKey());
      JsonElement element = entry.getValue();
      values.put(key, element);
      if (element.isJsonObject() && deep) {
        Map<PathString, Object> subValues = getValues(element.getAsJsonObject(), true);
        for (Map.Entry<PathString, Object> subEntry : subValues.entrySet()) {
          values.put(key.append(subEntry.getKey()), subEntry.getValue());
        }
      }
    }
    return values;
  }

  @Override
  public Object getOriginal() {
    return this.root;
  }

  private <T> T operateOnPath(PathString path, BiFunction<JsonObject, String, T> function) {
    JsonObject currentObject = this.root;
    String[] pathArray = path.getPath();
    if (pathArray.length == 0) {
      return function.apply(currentObject, "");
    }
    for (int i = 0; i < pathArray.length; i++) {
      String key = pathArray[i];
      if (i == pathArray.length - 1) {
        return function.apply(currentObject, key);
      } else {
        JsonElement element = currentObject.get(key);
        if (element.isJsonObject()) {
          currentObject = element.getAsJsonObject();
        } else {
          JsonObject newObject = new JsonObject();
          currentObject.add(key, newObject);
          currentObject = newObject;
        }
      }
    }
    return null;
  }

  @Override
  public Object get(PathString path, Object def) {
    return operateOnPath(path, JsonObject::get);
  }

  @Override
  public void set(PathString path, Object value) {
    operateOnPath(path, (object, key) -> {
      object.add(key, GSON.toJsonTree(value));
      return null;
    });
  }

  @Override
  public String getName() {
    return file.getName();
  }

  @Override
  public Map<PathString, Object> getValues(PathString path, boolean deep) {
    return operateOnPath(path, (object, key) -> {
      JsonElement element = object.get(key);
      if (element.isJsonObject()) {
        return getValues(element.getAsJsonObject(), deep);
      } else {
        return Collections.emptyMap();
      }
    });
  }

  @Override
  public void setup() {
    if (!this.file.exists()) {
      File parentFile = this.file.getAbsoluteFile().getParentFile();
      if (parentFile != null && !parentFile.exists()) {
        parentFile.mkdirs();
      }
      try {
        this.file.createNewFile();
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, e, () -> "Something wrong when creating " + this.file.getName());
      }
    }

    try (FileReader reader = new FileReader(file)) {
      JsonElement jsonElement = JsonParser.parseReader(reader);
      if (jsonElement.isJsonObject()) {
        this.root = jsonElement.getAsJsonObject();
      }
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when setting up " + file.getName());
    }
  }

  @Override
  public void save() {
    try (
      FileWriter writer = new FileWriter(file);
      JsonWriter jsonWriter = new JsonWriter(writer)
    ) {
      GSON.toJson(this.root, jsonWriter);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when saving " + file.getName());
    }
  }

  @Override
  public void reload() {
    this.root = new JsonObject();
    this.setup();
  }

  @Override
  public Object normalize(Object object) {
    if (!(object instanceof JsonElement)) {
      return object;
    }

    JsonElement jsonElement = (JsonElement) object;
    if (jsonElement.isJsonPrimitive()) {
      JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
      if (jsonPrimitive.isBoolean()) {
        return jsonPrimitive.getAsBoolean();
      } else if (jsonPrimitive.isNumber()) {
        return jsonPrimitive.getAsNumber();
      } else if (jsonPrimitive.isString()) {
        return jsonPrimitive.getAsString();
      }
    } else if (jsonElement.isJsonArray()) {
      JsonArray jsonArray = jsonElement.getAsJsonArray();
      List<Object> list = new ArrayList<>();
      for (JsonElement element : jsonArray) {
        list.add(this.normalize(element));
      }
      return list;
    } else if (jsonElement.isJsonObject()) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      Map<String, Object> map = new LinkedHashMap<>();
      for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
        map.put(entry.getKey(), this.normalize(entry.getValue()));
      }
      return map;
    } else if (jsonElement.isJsonNull()) {
      return null;
    }
    return jsonElement.getAsString();
  }

  @Override
  public boolean isNormalizable(Object object) {
    return object instanceof JsonElement;
  }
}
