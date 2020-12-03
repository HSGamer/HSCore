package me.hsgamer.hscore.json;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;

/**
 * An simple JSON Utils, using json-simple
 */
public class JSONUtils {

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
   * Get JSON value from a file
   *
   * @param file the file
   *
   * @return the JSON value, or null if there is an I/O error or the file does not exist
   */
  @Nullable
  public static JsonValue getJSON(@NotNull File file) {
    try {
      return getJSON(new FileReader(file));
    } catch (FileNotFoundException e) {
      return null;
    }
  }

  /**
   * Get JSON value from a reader
   *
   * @param reader the reader
   *
   * @return the JSON value, or null if there is an I/O error
   */
  @Nullable
  public static JsonValue getJSON(@NotNull Reader reader) {
    try {
      return Json.parse(reader);
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Get JSON value from a string
   *
   * @param string the string
   *
   * @return the JSON value, or null if there is an error when parsing
   */
  @NotNull
  public static JsonValue getJSON(@NotNull String string) {
    return Json.parse(string);
  }

  /**
   * Write JSON value to file
   *
   * @param file      the file
   * @param jsonValue the object
   *
   * @throws IOException if the file is not found or it's a directory or an I/O error occurred
   */
  public static void writeToFile(@NotNull File file, @NotNull JsonValue jsonValue) throws IOException {
    FileWriter writer = new FileWriter(file);
    jsonValue.writeTo(writer, WriterConfig.PRETTY_PRINT);
    writer.flush();
  }
}
