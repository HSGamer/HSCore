package me.hsgamer.hscore.addon.object;

import me.hsgamer.hscore.addon.exception.RequiredAddonPathException;
import me.hsgamer.hscore.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A path to get value from the addon config file
 *
 * @param <T> the type of the final value
 */
public abstract class AddonPath<T> {

  /**
   * The path to get the value
   */
  @NotNull
  private final String path;

  /**
   * Is the path required or not
   */
  private final boolean required;

  /**
   * Create an addon path
   *
   * @param path     the path to the value
   * @param required is it required to be in the addon config file
   */
  public AddonPath(@NotNull final String path, final boolean required) {
    this.path = path;
    this.required = required;
  }

  /**
   * Check if the path is required to be in the addon config file
   *
   * @return is it required to be in the addon config file
   */
  public final boolean isRequired() {
    return this.required;
  }

  /**
   * Get the path to the value
   *
   * @return the path
   */
  @NotNull
  public final String getPath() {
    return this.path;
  }

  /**
   * Convert the type of the value from the addon config file
   *
   * @param object the raw value from the addon config file
   *
   * @return the converted value
   */
  @Nullable
  public abstract T convertType(@NotNull Object object);

  /**
   * Get the value from the addon config file
   *
   * @param addon the addon
   *
   * @return the value
   *
   * @throws RequiredAddonPathException if the path is required but is not found in the addon config file
   */
  @Nullable
  public final T get(@NotNull final Addon addon) {
    final Config config = addon.getDescription().getConfiguration();
    if (this.required && !config.contains(this.path)) {
      throw new RequiredAddonPathException(
        this.path + " is not found in the addon '" + addon.getDescription().getName() + "'");
    }
    final Object value = config.get(this.path);
    return value != null ? this.convertType(value) : null;
  }
}
