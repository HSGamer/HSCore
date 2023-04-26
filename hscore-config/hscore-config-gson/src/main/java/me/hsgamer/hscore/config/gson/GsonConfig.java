package me.hsgamer.hscore.config.gson;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * The {@link Config} implementation for Gson
 */
public class GsonConfig implements Config {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private final File file;
  private JsonObject jsonObject = new JsonObject();

  /**
   * Create a new config
   *
   * @param file the file
   */
  public GsonConfig(File file) {
    this.file = file;
  }

  @Override
  public Object getOriginal() {
    return this.jsonObject;
  }

  @Override
  public Object get(PathString path, Object def) {
    return null;
  }

  @Override
  public void set(PathString path, Object value) {

  }

  @Override
  public String getName() {
    return file.getName();
  }

  @Override
  public Map<PathString, Object> getValues(PathString path, boolean deep) {
    return null;
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
        this.jsonObject = jsonElement.getAsJsonObject();
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
      GSON.toJson(this.jsonObject, jsonWriter);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when saving " + file.getName());
    }
  }

  @Override
  public void reload() {
    this.jsonObject = new JsonObject();
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
