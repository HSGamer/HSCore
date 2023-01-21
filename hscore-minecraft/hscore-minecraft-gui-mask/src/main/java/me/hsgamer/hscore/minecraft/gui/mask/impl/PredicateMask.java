package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Predicate;

/**
 * The mask with predicate
 */
public class PredicateMask extends BaseMask {
  private final Set<UUID> failToViewList = new ConcurrentSkipListSet<>();
  private Predicate<UUID> viewPredicate = uuid -> true;
  private Mask mask;
  private Mask fallbackMask;

  /**
   * Create a new mask
   *
   * @param name          the name of the mask
   * @param viewPredicate the view predicate
   * @param mask          the mask
   * @param fallbackMask  the fallback mask
   */
  public PredicateMask(@NotNull String name, @NotNull Predicate<@NotNull UUID> viewPredicate, @NotNull Mask mask, @NotNull Mask fallbackMask) {
    super(name);
    this.viewPredicate = viewPredicate;
    this.mask = mask;
    this.fallbackMask = fallbackMask;
  }

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  public PredicateMask(@NotNull String name) {
    this(name, uuid -> true, Mask.empty(name + "_empty"), Mask.empty(name + "_empty_fallback"));
  }

  /**
   * Set the view predicate
   *
   * @param viewPredicate the view predicate
   */
  public void setViewPredicate(@NotNull Predicate<@NotNull UUID> viewPredicate) {
    this.viewPredicate = viewPredicate;
  }

  /**
   * Get the mask
   *
   * @return the mask
   */
  @NotNull
  public Mask getMask() {
    return mask;
  }

  /**
   * Set the mask
   *
   * @param mask the mask
   */
  public void setMask(@NotNull Mask mask) {
    this.mask = mask;
  }

  /**
   * Get the fallback mask
   *
   * @return the fallback mask
   */
  @NotNull
  public Mask getFallbackMask() {
    return fallbackMask;
  }

  /**
   * Set the fallback mask
   *
   * @param fallbackMask the fallback mask
   */
  public void setFallbackMask(@NotNull Mask fallbackMask) {
    this.fallbackMask = fallbackMask;
  }

  @Override
  public boolean canView(@NotNull UUID uuid) {
    if (viewPredicate.test(uuid)) {
      failToViewList.remove(uuid);
      return mask != null && mask.canView(uuid);
    } else {
      failToViewList.add(uuid);
      return fallbackMask != null && fallbackMask.canView(uuid);
    }
  }

  @Override
  public @NotNull Map<Integer, Button> generateButtons(@NotNull UUID uuid) {
    if (failToViewList.contains(uuid)) {
      return fallbackMask != null ? fallbackMask.generateButtons(uuid) : Collections.emptyMap();
    } else {
      return mask != null ? mask.generateButtons(uuid) : Collections.emptyMap();
    }
  }

  @Override
  public void init() {
    if (mask != null) {
      mask.init();
    }
    if (fallbackMask != null) {
      fallbackMask.init();
    }
  }

  @Override
  public void stop() {
    if (mask != null) {
      mask.stop();
    }
    if (fallbackMask != null) {
      fallbackMask.stop();
    }
  }
}
