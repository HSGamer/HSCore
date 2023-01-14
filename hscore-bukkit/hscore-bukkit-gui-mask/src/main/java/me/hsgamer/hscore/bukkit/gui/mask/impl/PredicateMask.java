package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.bukkit.gui.mask.BaseMask;
import me.hsgamer.hscore.bukkit.gui.mask.Mask;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Predicate;

/**
 * A mask with predicate
 */
public class PredicateMask extends BaseMask {
  private final Set<UUID> failToViewList = new ConcurrentSkipListSet<>();
  private Predicate<UUID> viewPredicate = uuid -> true;
  private Mask mask;
  private Mask fallbackMask;

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  public PredicateMask(String name) {
    super(name);
  }

  /**
   * Create a new mask
   *
   * @param name          the name of the mask
   * @param viewPredicate the view predicate
   * @param mask          the mask
   * @param fallbackMask  the fallback mask
   */
  public PredicateMask(String name, Predicate<UUID> viewPredicate, Mask mask, Mask fallbackMask) {
    super(name);
    this.viewPredicate = viewPredicate;
    this.mask = mask;
    this.fallbackMask = fallbackMask;
  }

  /**
   * Set the view predicate
   *
   * @param viewPredicate the view predicate
   */
  public void setViewPredicate(Predicate<UUID> viewPredicate) {
    this.viewPredicate = viewPredicate;
  }

  /**
   * Get the mask
   *
   * @return the mask
   */
  public Mask getMask() {
    return mask;
  }

  /**
   * Set the mask
   *
   * @param mask the mask
   */
  public void setMask(Mask mask) {
    this.mask = mask;
  }

  /**
   * Get the fallback mask
   *
   * @return the fallback mask
   */
  public Mask getFallbackMask() {
    return fallbackMask;
  }

  /**
   * Set the fallback mask
   *
   * @param fallbackMask the fallback mask
   */
  public void setFallbackMask(Mask fallbackMask) {
    this.fallbackMask = fallbackMask;
  }

  @Override
  public boolean canView(UUID uuid) {
    if (viewPredicate.test(uuid)) {
      failToViewList.remove(uuid);
      return mask != null && mask.canView(uuid);
    } else {
      failToViewList.add(uuid);
      return fallbackMask != null && fallbackMask.canView(uuid);
    }
  }

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
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
