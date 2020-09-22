package me.hsgamer.hscore.addon.object;

import me.hsgamer.hscore.addon.exception.RequiredAddonPathException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.YamlConfiguration;

/**
 * A path to get value from addon.yml
 *
 * @param <T> the type of the final value
 */
public abstract class AddonPath<T> {

  private final String path;
  private final boolean required;

  /**
   * Create an addon path
   *
   * @param path     the path to the value
   * @param required is it required to be in addon.yml
   */
  public AddonPath(@NotNull String path, boolean required) {
    this.path = path;
    this.required = required;
  }

  /**
   * Check if the path is required to be in addon.yml
   *
   * @return is it required to be in addon.yml
   */
  public final boolean isRequired() {
    return required;
  }

  /**
   * Get the path to the value
   *
   * @return the path
   */
  @NotNull
  public final String getPath() {
    return path;
  }

  /**
   * Convert the type of the value from addon.yml
   *
   * @param object the raw value from addon.yml
   * @return the converted value
   */
  @Nullable
  public abstract T convertType(@NotNull Object object);

  /**
   * Get the value from addon.yml
   *
   * @param addon the addon
   * @return the value
   * @throws RequiredAddonPathException if the path is required but is not found in addon.yml
   */
  @Nullable
  public T get(@NotNull Addon addon) {
    YamlConfiguration configuration = addon.getDescription().getConfiguration();
    if (required && !configuration.isSet(path)) {
      throw new RequiredAddonPathException(
        path + " is not found in the addon '" + addon.getDescription().getName() + "'");
    }

    Object value = configuration.get(path);
    return value != null ? convertType(value) : null;
  }
}
