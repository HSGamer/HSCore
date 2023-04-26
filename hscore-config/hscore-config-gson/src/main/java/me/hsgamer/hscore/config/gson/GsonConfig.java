package me.hsgamer.hscore.config.gson;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

  private Optional<JsonObject> getJsonObject(PathString path, boolean createIfNotFound) {
    JsonObject currentObject = this.root;
    if (path.isRoot()) {
      return Optional.of(currentObject);
    }
    String[] pathArray = path.getPath();
    for (int i = 0; i < pathArray.length; i++) {
      String key = pathArray[i];
      if (i == pathArray.length - 1) {
        return Optional.of(currentObject);
      } else {
        JsonElement element = currentObject.has(key) ? currentObject.get(key) : JsonNull.INSTANCE;
        if (element.isJsonObject()) {
          currentObject = element.getAsJsonObject();
        } else if (createIfNotFound) {
          JsonObject newObject = new JsonObject();
          currentObject.add(key, newObject);
          currentObject = newObject;
        } else {
          return Optional.empty();
        }
      }
    }
    return Optional.empty();
  }

  @Override
  public Object get(PathString path, Object def) {
    if (path.isRoot()) {
      return this.root;
    }
    return getJsonObject(path, false).<Object>map(object -> {
      String lastPath = path.getLastPath();
      if (object.has(lastPath)) {
        return object.get(lastPath);
      }
      return null;
    }).orElseGet(() -> {
      if (def == null) {
        return null;
      }
      if (def instanceof JsonElement) {
        return def;
      }
      return GSON.toJsonTree(def);
    });
  }

  @Override
  public void set(PathString path, Object value) {
    JsonElement element = value instanceof JsonElement ? (JsonElement) value : GSON.toJsonTree(value);
    if (path.isRoot()) {
      if (element.isJsonObject()) {
        this.root = element.getAsJsonObject();
      }
      return;
    }
    getJsonObject(path, true).ifPresent(object -> {
      if (element.isJsonNull()) {
        object.remove(path.getLastPath());
      } else {
        object.add(path.getLastPath(), element);
      }
    });
  }

  @Override
  public void clear() {
    this.root = new JsonObject();
  }

  @Override
  public String getName() {
    return file.getName();
  }

  @Override
  public Map<PathString, Object> getValues(PathString path, boolean deep) {
    if (path.isRoot()) {
      return getValues(this.root, deep);
    }
    return getJsonObject(path, false).map(object -> getValues(object, deep)).orElse(Collections.emptyMap());
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
    if (!isNormalizable(object)) {
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
      return jsonElement.getAsJsonArray().asList();
    } else if (jsonElement.isJsonObject()) {
      return jsonElement.getAsJsonObject().asMap();
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
