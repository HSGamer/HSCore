package me.hsgamer.hscore.downloader.object;

import me.hsgamer.hscore.downloader.exception.RequiredInfoKeyException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;

/**
 * A key to get value from the download info
 *
 * @param <T> the type of the final value
 */
public abstract class InfoKey<T> {

  /**
   * The key to get the value
   */
  @NotNull
  private final String key;

  /**
   * Is the key required or not
   */
  private final boolean required;

  /**
   * Create an info key
   *
   * @param key the key to the value
   * @param required the required to the value
   */
  public InfoKey(@NotNull final String key, final boolean required) {
    this.key = key;
    this.required = required;
  }

  /**
   * Get the key to the value
   *
   * @return the key
   */
  @NotNull
  public final String getKey() {
    return this.key;
  }

  /**
   * Check if the key is required to be in the download info
   *
   * @return is it required to be in the download info
   */
  public final boolean isRequired() {
    return this.required;
  }

  /**
   * Convert the type of the value from the raw value
   *
   * @param object the raw value
   *
   * @return the converted value
   */
  @Nullable
  public abstract T convertType(@NotNull Object object);

  /**
   * Get the value from the download info
   *
   * @param downloadInfo the download info
   *
   * @return the value
   */
  @Nullable
  public final T get(@NotNull final DownloadInfo downloadInfo) {
    JSONObject jsonObject = downloadInfo.getJsonObject();
    if (this.required && !jsonObject.containsKey(key)) {
      throw new RequiredInfoKeyException(this.key + " is not found in the download info '" + downloadInfo.getName() + "'");
    }
    final Object value = jsonObject.get(key);
    return value != null ? this.convertType(value) : null;
  }
}
