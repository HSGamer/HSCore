package me.hsgamer.hscore.expansion.common;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

/**
 * The description for the {@link Expansion}
 */
public interface ExpansionDescription {

  /**
   * Create a simple {@link ExpansionDescription}
   *
   * @param name      the name
   * @param version   the version
   * @param mainClass the main class
   * @param data      the extra data
   *
   * @return the {@link ExpansionDescription}
   */
  static ExpansionDescription simple(@NotNull final String name, @NotNull final String version, @NotNull final String mainClass, @NotNull final Map<String, Object> data) {
    return new ExpansionDescription() {
      @Override
      public @NotNull String getName() {
        return name;
      }

      @Override
      public @NotNull String getVersion() {
        return version;
      }

      @Override
      public @NotNull String getMainClass() {
        return mainClass;
      }

      @Override
      public @NotNull Map<String, Object> getData() {
        return data;
      }
    };
  }

  /**
   * Get the name of the expansion
   *
   * @return the name
   */
  @NotNull
  String getName();

  /**
   * Get the version of the expansion
   *
   * @return the version
   */
  @NotNull
  String getVersion();

  /**
   * Get the main class of the expansion
   *
   * @return the path to the main class
   */
  @NotNull
  String getMainClass();

  /**
   * Get the extra data of the expansion
   *
   * @return the data
   */
  @NotNull
  default Map<String, Object> getData() {
    return Collections.emptyMap();
  }
}
