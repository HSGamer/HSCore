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

import me.hsgamer.hscore.addon.loader.AddonDescriptionLoader;
import me.hsgamer.hscore.addon.object.Addon;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;

/**
 * A simple implementation for the addon manager
 */
public final class SimpleAddonManager extends AddonManager {

  /**
   * The sort and filter function
   */
  @NotNull
  private final UnaryOperator<Map<String, Addon>> sortAndFilter;

  /**
   * The on addon loading predicate
   */
  @NotNull
  private final Predicate<Addon> onAddonLoading;

  /**
   * Create a simple addon manager
   *
   * @param addonsDir              the addon directory
   * @param logger                 the logger
   * @param addonDescriptionLoader the addon description loader
   * @param sortAndFilter          the sort and filter
   * @param onAddonLoading         the on addon loading
   */
  public SimpleAddonManager(@NotNull final File addonsDir, @NotNull final Logger logger,
                            @NotNull final AddonDescriptionLoader addonDescriptionLoader,
                            @NotNull final UnaryOperator<Map<String, Addon>> sortAndFilter,
                            @NotNull final Predicate<Addon> onAddonLoading) {
    super(addonsDir, logger, addonDescriptionLoader);
    this.sortAndFilter = sortAndFilter;
    this.onAddonLoading = onAddonLoading;
  }

  /**
   * Create a simple addon manager
   *
   * @param addonsDir              the addon directory
   * @param logger                 the logger
   * @param addonDescriptionLoader the addon description loader
   * @param onAddonLoading         the on addon loading
   */
  public SimpleAddonManager(@NotNull final File addonsDir, @NotNull final Logger logger,
                            @NotNull final AddonDescriptionLoader addonDescriptionLoader,
                            @NotNull final Predicate<Addon> onAddonLoading) {
    this(addonsDir, logger, addonDescriptionLoader, map -> map, onAddonLoading);
  }

  /**
   * Create a simple addon manager
   *
   * @param addonsDir              the addon directory
   * @param logger                 the logger
   * @param addonDescriptionLoader the addon description loader
   * @param sortAndFilter          the sort and filter
   */
  public SimpleAddonManager(@NotNull final File addonsDir, @NotNull final Logger logger,
                            @NotNull final AddonDescriptionLoader addonDescriptionLoader,
                            @NotNull final UnaryOperator<Map<String, Addon>> sortAndFilter) {
    this(addonsDir, logger, addonDescriptionLoader, sortAndFilter, addon -> true);
  }

  @NotNull
  @Override
  protected Map<String, Addon> sortAndFilter(@NotNull final Map<String, Addon> original) {
    return this.sortAndFilter.apply(original);
  }

  @Override
  protected boolean onAddonLoading(@NotNull final Addon addon) {
    return this.onAddonLoading.test(addon);
  }
}
