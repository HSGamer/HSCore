package me.hsgamer.hscore.json;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
   * @return the JSON file
   * @throws IOException If an I/O error occurred
   */
  public static File createFile(String filename, File path) throws IOException {
    if (!filename.endsWith(".json")) {
      filename += ".json";
    }

    File file = new File(path, filename);
    if (!file.exists()) {
      file.createNewFile();
      Files.write(file.toPath(), Collections.singletonList("{}"));
    }
    return file;
  }

  /**
   * Get JSON object from a file
   *
   * @param file the file
   * @return the JSON object, or null if there is an error when parsing
   */
  public static Object getJSON(File file) {
    try (FileReader reader = new FileReader(file)) {
      return parser.parse(reader);
    } catch (IOException | ParseException e) {
      return null;
    }
  }

  /**
   * Get JSON object from a string
   *
   * @param string the string
   * @return the JSON object, or null if there is an error when parsing
   */
  public static Object getJSON(String string) {
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
   * @throws IOException if the file is not found or it's a directory or an I/O error occurred
   */
  public static void writeToFile(File file, JSONObject jsonObject) throws IOException {
    FileWriter writer = new FileWriter(file);
    jsonObject.writeJSONString(writer);
    writer.flush();
  }
}
