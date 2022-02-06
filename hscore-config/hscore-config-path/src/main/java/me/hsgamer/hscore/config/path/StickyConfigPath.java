package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.common.CachedValue;
import me.hsgamer.hscore.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link ConfigPath} which stores the value for later uses
 *
 * @param <T> the type of the value
 */
public class StickyConfigPath<T> extends CachedValue<T> implements ConfigPath<T> {
  private final ConfigPath<T> originalPath;

  /**
   * Create a config path
   *
   * @param originalPath the original config path
   */
  public StickyConfigPath(ConfigPath<T> originalPath) {
    this.originalPath = originalPath;
  }

  @Override
  public T generate() {
    return this.originalPath.getValue();
  }

  @Override
  public void reload() {
    this.originalPath.reload();
    this.clearCache();
  }

  @Override
  public void setValue(@Nullable T value) {
    this.originalPath.setValue(value);
    this.clearCache();
  }

  @Override
  public T getValue(@NotNull Config config) {
    return originalPath.getValue(config);
  }

  @Override
  public void setValue(@Nullable T value, @NotNull Config config) {
    originalPath.setValue(value, config);
  }

  @NotNull
  @Override
  public String getPath() {
    return this.originalPath.getPath();
  }

  @Nullable
  @Override
  public Config getConfig() {
    return this.originalPath.getConfig();
  }

  @Override
  public void setConfig(@NotNull Config config) {
    this.originalPath.setConfig(config);
    this.clearCache();
  }
}
