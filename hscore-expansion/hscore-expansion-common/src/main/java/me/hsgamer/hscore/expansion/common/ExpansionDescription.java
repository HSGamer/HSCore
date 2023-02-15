package me.hsgamer.hscore.expansion.common;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * The description for the Addon
 */
public final class ExpansionDescription {

  /**
   * The name of the addon
   */
  @NotNull
  private final String name;

  /**
   * The version of the addon
   */
  @NotNull
  private final String version;

  /**
   * The main class of the addon
   */
  @NotNull
  private final String mainClass;

  /**
   * The data of the addon
   */
  @NotNull
  private final Map<String, Object> data;

  /**
   * Create an addon description
   *
   * @param name      the name of the addon
   * @param version   the version of the addon
   * @param mainClass the main class of the addon
   * @param data      the data of the addon
   */
  public ExpansionDescription(@NotNull final String name, @NotNull final String version, @NotNull final String mainClass,
                              @NotNull final Map<String, Object> data) {
    this.name = name;
    this.version = version;
    this.mainClass = mainClass;
    this.data = data;
  }

  /**
   * Get the name of the addon
   *
   * @return the name
   */
  @NotNull
  public String getName() {
    return this.name;
  }

  /**
   * Get the version of the addon
   *
   * @return the version
   */
  @NotNull
  public String getVersion() {
    return this.version;
  }

  /**
   * Get the main class of the addon
   *
   * @return the path to the main class
   */
  @NotNull
  public String getMainClass() {
    return this.mainClass;
  }

  /**
   * Get the data of the addon
   *
   * @return the data
   */
  @NotNull
  public Map<String, Object> getData() {
    return data;
  }
}
