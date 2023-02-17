package me.hsgamer.hscore.expansion.common;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * The description for the {@link Expansion}
 */
public final class ExpansionDescription {

  /**
   * The name of the expansion
   */
  @NotNull
  private final String name;

  /**
   * The version of the expansion
   */
  @NotNull
  private final String version;

  /**
   * The main class of the expansion
   */
  @NotNull
  private final String mainClass;

  /**
   * The extra data of the expansion
   */
  @NotNull
  private final Map<String, Object> data;

  /**
   * Create an expansion description
   *
   * @param name      the name of the expansion
   * @param version   the version of the expansion
   * @param mainClass the main class of the expansion
   * @param data      the extra data of the expansion
   */
  public ExpansionDescription(@NotNull final String name, @NotNull final String version, @NotNull final String mainClass,
                              @NotNull final Map<String, Object> data) {
    this.name = name;
    this.version = version;
    this.mainClass = mainClass;
    this.data = data;
  }

  /**
   * Get the name of the expansion
   *
   * @return the name
   */
  @NotNull
  public String getName() {
    return this.name;
  }

  /**
   * Get the version of the expansion
   *
   * @return the version
   */
  @NotNull
  public String getVersion() {
    return this.version;
  }

  /**
   * Get the main class of the expansion
   *
   * @return the path to the main class
   */
  @NotNull
  public String getMainClass() {
    return this.mainClass;
  }

  /**
   * Get the extra data of the expansion
   *
   * @return the data
   */
  @NotNull
  public Map<String, Object> getData() {
    return data;
  }
}
