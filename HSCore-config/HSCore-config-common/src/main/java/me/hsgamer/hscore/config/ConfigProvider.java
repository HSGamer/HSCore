package me.hsgamer.hscore.config;

import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.*;

/**
 * Config provider for {@link Config}
 *
 * @param <F> File configuration class type
 */
public interface ConfigProvider<F extends FileConfiguration> {

  /**
   * Load configuration from a file
   *
   * @param file the file
   *
   * @return the configuration
   */
  F loadConfiguration(File file);

  /**
   * Load configuration from a reader
   *
   * @param reader the reader
   *
   * @return the configuration
   *
   * @throws IOException if there is an I/O error
   */
  default F loadConfiguration(Reader reader) throws IOException {
    File tempFile = File.createTempFile("tempAddonConfigFile", null);
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));
    BufferedReader bufferedReader = new BufferedReader(reader);

    String line = bufferedReader.readLine();
    while (line != null) {
      bufferedWriter.write(line);
      line = bufferedReader.readLine();
    }

    F config = loadConfiguration(tempFile);

    bufferedReader.close();
    bufferedWriter.close();
    tempFile.deleteOnExit();

    return config;
  }

  /**
   * Save the configuration to the file
   *
   * @param fileConfiguration the configuration
   * @param file              the file
   *
   * @throws IOException if there is an I/O error occurred
   */
  default void saveConfiguration(F fileConfiguration, File file) throws IOException {
    fileConfiguration.save(file);
  }
}
