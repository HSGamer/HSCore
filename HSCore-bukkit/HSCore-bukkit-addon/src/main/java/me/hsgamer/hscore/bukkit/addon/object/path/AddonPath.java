package me.hsgamer.hscore.bukkit.addon.object.path;

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
  public AddonPath(String path, boolean required) {
    this.path = path;
    this.required = required;
  }

  /**
   * Check if the path is required to be in addon.yml
   *
   * @return is it required to be in addon.yml
   */
  public boolean isRequired() {
    return required;
  }

  /**
   * Get the path to the value
   *
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * Convert the type of the value from addon.yml
   *
   * @param object the raw value from addon.yml
   * @return the converted value
   */
  public abstract T convertType(Object object);
}
