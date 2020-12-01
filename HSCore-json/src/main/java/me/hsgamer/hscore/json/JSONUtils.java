package me.hsgamer.hscore.json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;

/**
 * An simple JSON Utils, using json-simple
 */
public class JSONUtils {

  private static final JSONParser parser = new JSONParser();

  private JSONUtils() {
  }

  /**
   * Create an JSON file
   *
   * @param filename the name of the file
   * @param path     the path to the file
   *
   * @return the JSON file
   *
   * @throws IOException If an I/O error occurred
   */
  @NotNull
  public static File createFile(@NotNull String filename, @NotNull File path) throws IOException {
    if (!filename.endsWith(".json")) {
      filename += ".json";
    }

    File file = new File(path, filename);
    if (!file.exists() && file.createNewFile()) {
      Files.write(file.toPath(), Collections.singletonList("{}"));
    }
    return file;
  }

  /**
   * Get JSON object from a file
   *
   * @param file the file
   *
   * @return the JSON object, or null if there is an error when parsing
   */
  @Nullable
  public static Object getJSON(@NotNull File file) {
    try {
      return getJSON(new FileReader(file));
    } catch (FileNotFoundException e) {
      return null;
    }
  }

  /**
   * Get JSON object from a reader
   *
   * @param reader the reader
   *
   * @return the JSON object, or null if there is an error when parsing
   */
  @Nullable
  public static Object getJSON(@NotNull Reader reader) {
    try {
      return parser.parse(reader);
    } catch (IOException | ParseException e) {
      return null;
    }
  }

  /**
   * Get JSON object from a string
   *
   * @param string the string
   *
   * @return the JSON object, or null if there is an error when parsing
   */
  @Nullable
  public static Object getJSON(@NotNull String string) {
    try {
      return parser.parse(string);
    } catch (ParseException e) {
      return null;
    }
  }

  /**
   * Write JSON object to file
   *
   * @param file       the file
   * @param jsonObject the object
   *
   * @throws IOException if the file is not found or it's a directory or an I/O error occurred
   */
  public static void writeToFile(@NotNull File file, @NotNull JSONObject jsonObject) throws IOException {
    FileWriter writer = new FileWriter(file);
    jsonObject.writeJSONString(writer);
    writer.flush();
  }
}
