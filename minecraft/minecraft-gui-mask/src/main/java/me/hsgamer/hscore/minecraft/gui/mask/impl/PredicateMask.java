package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * The mask with predicate
 */
public class PredicateMask extends BaseMask {
  private Predicate<UUID> viewPredicate = uuid -> true;
  private Mask mask;
  private Mask fallbackMask;

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  public PredicateMask(@NotNull String name) {
    super(name);
    this.mask = Mask.empty(name + "_empty");
    this.fallbackMask = Mask.empty(name + "_empty_fallback");
  }

  /**
   * Set the view predicate
   *
   * @param viewPredicate the view predicate
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public PredicateMask setViewPredicate(@NotNull Predicate<@NotNull UUID> viewPredicate) {
    this.viewPredicate = viewPredicate;
    return this;
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
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public PredicateMask setMask(@NotNull Mask mask) {
    this.mask = mask;
    return this;
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
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public PredicateMask setFallbackMask(@NotNull Mask fallbackMask) {
    this.fallbackMask = fallbackMask;
    return this;
  }

  @Override
  public Optional<Map<Integer, Button>> generateButtons(@NotNull UUID uuid, @NotNull InventorySize inventorySize) {
    if (viewPredicate.test(uuid)) {
      return mask != null ? mask.generateButtons(uuid, inventorySize) : Optional.empty();
    } else {
      return fallbackMask != null ? fallbackMask.generateButtons(uuid, inventorySize) : Optional.empty();
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
