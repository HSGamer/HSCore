package me.hsgamer.hscore.config.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.gson.GsonUtils;
import me.hsgamer.hscore.logger.common.LogLevel;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static me.hsgamer.hscore.config.PathString.asArray;
import static me.hsgamer.hscore.config.PathString.concat;

/**
 * The {@link Config} implementation for Gson
 */
public class GsonConfig implements Config {
  private final Gson gson;
  private final File file;
  private JsonObject root = new JsonObject();

  /**
   * Create a new config
   *
   * @param file the file
   * @param gson the Gson instance
   */
  public GsonConfig(File file, Gson gson) {
    this.file = file;
    this.gson = gson;
  }

  /**
   * Create a new config
   *
   * @param file the file
   */
  public GsonConfig(File file) {
    this(file, new Gson());
  }

  private static Map<String[], Object> getValues(JsonObject object, boolean deep) {
    Map<String[], Object> values = new HashMap<>();
    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
      String[] key = asArray(entry.getKey());
      JsonElement element = entry.getValue();
      values.put(key, element);
      if (element.isJsonObject() && deep) {
        Map<String[], Object> subValues = getValues(element.getAsJsonObject(), true);
        for (Map.Entry<String[], Object> subEntry : subValues.entrySet()) {
          values.put(concat(key, subEntry.getKey()), subEntry.getValue());
        }
      }
    }
    return values;
  }

  @Override
  public JsonObject getOriginal() {
    return this.root;
  }

  private Optional<JsonObject> getJsonObject(boolean createIfNotFound, String... path) {
    JsonObject currentObject = this.root;
    if (path.length == 0) {
      return Optional.of(currentObject);
    }
    for (int i = 0; i < path.length; i++) {
      String key = path[i];
      if (i == path.length - 1) {
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
  public Object get(Object def, String... path) {
    if (path.length == 0) {
      return this.root;
    }
    return getJsonObject(false, path).<Object>map(object -> {
      String lastPath = path[path.length - 1];
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
      return gson.toJsonTree(def);
    });
  }

  @Override
  public void set(Object value, String... path) {
    JsonElement element = value instanceof JsonElement ? (JsonElement) value : gson.toJsonTree(value);
    if (path.length == 0) {
      if (element.isJsonObject()) {
        this.root = element.getAsJsonObject();
      }
      return;
    }
    getJsonObject(true, path).ifPresent(object -> {
      String lastPath = path[path.length - 1];
      if (element.isJsonNull()) {
        object.remove(lastPath);
      } else {
        object.add(lastPath, element);
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
  public Map<String[], Object> getValues(boolean deep, String... path) {
    if (path.length == 0) {
      return getValues(this.root, deep);
    }
    return getJsonObject(false, path).map(object -> getValues(object, deep)).orElse(Collections.emptyMap());
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
        LOGGER.log(LogLevel.WARN, "Something wrong when creating " + file.getName(), e);
      }
    }

    try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
      JsonElement jsonElement = GsonUtils.parse(reader);
      if (jsonElement.isJsonObject()) {
        this.root = jsonElement.getAsJsonObject();
      }
    } catch (IOException e) {
      LOGGER.log(LogLevel.WARN, "Something wrong when loading " + file.getName(), e);
    }
  }

  @Override
  public void save() {
    try (
      OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8);
      JsonWriter jsonWriter = gson.newJsonWriter(writer)
    ) {
      Streams.write(this.root, jsonWriter);
    } catch (IOException e) {
      LOGGER.log(LogLevel.WARN, "Something wrong when saving " + file.getName(), e);
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
    return GsonUtils.normalize((JsonElement) object, false);
  }

  @Override
  public boolean isNormalizable(Object object) {
    return object instanceof JsonElement;
  }
}
