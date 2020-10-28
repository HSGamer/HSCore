/*
 * MIT License
 *
 * Copyright (c) 2020 Shiru ka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package me.hsgamer.hscore.addon;

import java.io.File;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;
import me.hsgamer.hscore.addon.object.Addon;
import me.hsgamer.hscore.config.ConfigProvider;
import org.jetbrains.annotations.NotNull;

/**
 * A simple implementation for the addon manager
 */
public final class SimpleAddonManager extends AddonManager {

  /**
   * The file name supplier
   */
  @NotNull
  private final Supplier<String> fileName;

  /**
   * The sort and filter function
   */
  @NotNull
  private final UnaryOperator<Map<String, Addon>> sortAndFilter;

  /**
   * The config provider supplier
   */
  @NotNull
  private final Supplier<ConfigProvider<?>> configProvider;

  /**
   * The on addon loading predicate
   */
  @NotNull
  private final Predicate<Addon> onAddonLoading;

  /**
   * Create a simple addon manager
   *
   * @param addonsDir      the addon directory
   * @param logger         the logger
   * @param fileName       the file name
   * @param sortAndFilter  the sort and filter
   * @param configProvider the config provider
   * @param onAddonLoading the on addon loading
   */
  public SimpleAddonManager(@NotNull final File addonsDir, @NotNull final Logger logger,
                            @NotNull final Supplier<String> fileName,
                            @NotNull final UnaryOperator<Map<String, Addon>> sortAndFilter,
                            @NotNull final Supplier<ConfigProvider<?>> configProvider,
                            @NotNull final Predicate<Addon> onAddonLoading) {
    super(addonsDir, logger);
    this.fileName = fileName;
    this.sortAndFilter = sortAndFilter;
    this.configProvider = configProvider;
    this.onAddonLoading = onAddonLoading;
  }

  /**
   * Create a simple addon manager
   *
   * @param addonsDir      the addon directory
   * @param logger         the logger
   * @param fileName       the file name
   * @param sortAndFilter  the sort and filter
   * @param configProvider the config provider
   */
  public SimpleAddonManager(@NotNull final File addonsDir, @NotNull final Logger logger,
                            @NotNull final Supplier<String> fileName,
                            @NotNull final UnaryOperator<Map<String, Addon>> sortAndFilter,
                            @NotNull final Supplier<ConfigProvider<?>> configProvider) {
    this(addonsDir, logger, fileName, sortAndFilter, configProvider, addon -> true);
  }

  @NotNull
  @Override
  public String getAddonConfigFileName() {
    return this.fileName.get();
  }

  @NotNull
  @Override
  protected Map<String, Addon> sortAndFilter(@NotNull final Map<String, Addon> original) {
    return this.sortAndFilter.apply(original);
  }

  @NotNull
  @Override
  protected ConfigProvider<?> getConfigProvider() {
    return this.configProvider.get();
  }

  @Override
  protected boolean onAddonLoading(@NotNull final Addon addon) {
    return this.onAddonLoading.test(addon);
  }
}
