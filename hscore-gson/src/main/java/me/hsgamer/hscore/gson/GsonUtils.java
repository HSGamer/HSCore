package me.hsgamer.hscore.gson;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Some utilities for Gson
 */
public final class GsonUtils {
  private static final boolean IS_PARSER_STATIC;

  static {
    boolean isParserStatic;
    try {
      JsonParser.class.getDeclaredMethod("parseString", String.class);
      isParserStatic = true;
    } catch (NoSuchMethodException e) {
      isParserStatic = false;
    }
    IS_PARSER_STATIC = isParserStatic;
  }

  private GsonUtils() {
    // EMPTY
  }

  /**
   * Parse a string to {@link JsonElement}
   *
   * @param string the string
   *
   * @return the {@link JsonElement}
   */
  @SuppressWarnings("deprecation")
  public static JsonElement parse(String string) {
    if (IS_PARSER_STATIC) {
      return JsonParser.parseString(string);
    } else {
      JsonParser parser = new JsonParser();
      return parser.parse(string);
    }
  }

  /**
   * Parse a reader to {@link JsonElement}
   *
   * @param reader the reader
   *
   * @return the {@link JsonElement}
   */
  @SuppressWarnings("deprecation")
  public static JsonElement parse(Reader reader) {
    if (IS_PARSER_STATIC) {
      return JsonParser.parseReader(reader);
    } else {
      JsonParser parser = new JsonParser();
      return parser.parse(reader);
    }
  }

  /**
   * Parse a reader to {@link JsonElement}
   *
   * @param reader the reader
   *
   * @return the {@link JsonElement}
   */
  @SuppressWarnings("deprecation")
  public static JsonElement parse(JsonReader reader) {
    if (IS_PARSER_STATIC) {
      return JsonParser.parseReader(reader);
    } else {
      JsonParser parser = new JsonParser();
      return parser.parse(reader);
    }
  }

  /**
   * Get the elements from a {@link JsonArray}
   *
   * @param array the array
   *
   * @return the elements
   */
  public static List<JsonElement> getElements(JsonArray array) {
    List<JsonElement> elements = new ArrayList<>();
    for (JsonElement element : array) {
      elements.add(element);
    }
    return elements;
  }

  /**
   * Get the elements from a {@link JsonObject}
   *
   * @param object the object
   *
   * @return the elements
   */
  public static Map<String, JsonElement> getElements(JsonObject object) {
    Map<String, JsonElement> elements = new LinkedTreeMap<>();
    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
      elements.put(entry.getKey(), entry.getValue());
    }
    return elements;
  }

  /**
   * Normalize the {@link JsonElement} to the object
   *
   * @param element the element
   * @param deep    whether to normalize the nested elements
   *
   * @return the normalized object
   */
  public static Object normalize(JsonElement element, boolean deep) {
    if (element.isJsonPrimitive()) {
      JsonPrimitive jsonPrimitive = element.getAsJsonPrimitive();
      if (jsonPrimitive.isBoolean()) {
        return jsonPrimitive.getAsBoolean();
      } else if (jsonPrimitive.isNumber()) {
        return jsonPrimitive.getAsNumber();
      } else if (jsonPrimitive.isString()) {
        return jsonPrimitive.getAsString();
      }
    } else if (element.isJsonArray()) {
      JsonArray jsonArray = element.getAsJsonArray();
      if (deep) {
        List<Object> list = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
          list.add(normalize(jsonElement, true));
        }
        return list;
      } else {
        return getElements(jsonArray);
      }
    } else if (element.isJsonObject()) {
      JsonObject jsonObject = element.getAsJsonObject();
      if (deep) {
        Map<String, Object> map = new LinkedTreeMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
          map.put(entry.getKey(), normalize(entry.getValue(), true));
        }
        return map;
      } else {
        return getElements(jsonObject);
      }
    } else if (element.isJsonNull()) {
      return null;
    }
    return element.getAsString();
  }
}
