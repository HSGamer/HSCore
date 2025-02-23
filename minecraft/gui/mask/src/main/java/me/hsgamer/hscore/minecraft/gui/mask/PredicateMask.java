package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.GUIElement;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The mask with predicate
 */
public class PredicateMask implements GUIElement, Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> {
  private Predicate<UUID> viewPredicate = uuid -> true;
  private Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> mask = context -> null;
  private Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> fallbackMask = context -> null;

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
  public Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> getMask() {
    return mask;
  }

  /**
   * Set the mask
   *
   * @param mask the mask
   */
  public void setMask(@NotNull Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> mask) {
    this.mask = mask;
  }

  /**
   * Get the fallback mask
   *
   * @return the fallback mask
   */
  @NotNull
  public Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> getFallbackMask() {
    return fallbackMask;
  }

  /**
   * Set the fallback mask
   *
   * @param fallbackMask the fallback mask
   */
  public void setFallbackMask(@NotNull Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>> fallbackMask) {
    this.fallbackMask = fallbackMask;
  }

  @Override
  public void init() {
    GUIElement.handleIfElement(mask, GUIElement::init);
    GUIElement.handleIfElement(fallbackMask, GUIElement::init);
  }

  @Override
  public void stop() {
    GUIElement.handleIfElement(mask, GUIElement::stop);
    GUIElement.handleIfElement(fallbackMask, GUIElement::stop);
  }

  @Override
  public @Nullable Map<Integer, ActionItem> apply(@NotNull InventoryContext context) {
    if (viewPredicate.test(context.getViewerID())) {
      return mask.apply(context);
    } else {
      return fallbackMask.apply(context);
    }
  }
}
