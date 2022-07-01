package me.hsgamer.hscore.downloader.core.object;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
   * The default value
   */
  @NotNull
  private final T defaultValue;

  /**
   * Create an info key
   *
   * @param key          the key to the value
   * @param defaultValue the default value
   */
  public InfoKey(@NotNull final String key, @NotNull final T defaultValue) {
    this.key = key;
    this.defaultValue = defaultValue;
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
   * Get the default value
   *
   * @return the default value
   */
  @NotNull
  public final T getDefaultValue() {
    return defaultValue;
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
  @NotNull
  public final T get(@NotNull final DownloadInfo downloadInfo) {
    final Object value = downloadInfo.getData().get(this.key);
    if (value == null) {
      return defaultValue;
    }
    final T convertedValue = convertType(value);
    return convertedValue == null ? defaultValue : convertedValue;
  }
}
