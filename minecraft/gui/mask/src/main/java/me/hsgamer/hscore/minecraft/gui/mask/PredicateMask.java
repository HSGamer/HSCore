package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * The mask with predicate
 */
public class PredicateMask implements ButtonMap {
  private Predicate<UUID> viewPredicate = uuid -> true;
  private ButtonMap mask = ButtonMap.EMPTY;
  private ButtonMap fallbackMask = ButtonMap.EMPTY;

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
  public ButtonMap getMask() {
    return mask;
  }

  /**
   * Set the mask
   *
   * @param mask the mask
   */
  public void setMask(@NotNull ButtonMap mask) {
    this.mask = mask;
  }

  /**
   * Get the fallback mask
   *
   * @return the fallback mask
   */
  @NotNull
  public ButtonMap getFallbackMask() {
    return fallbackMask;
  }

  /**
   * Set the fallback mask
   *
   * @param fallbackMask the fallback mask
   */
  public void setFallbackMask(@NotNull ButtonMap fallbackMask) {
    this.fallbackMask = fallbackMask;
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

  @Override
  public @Nullable Map<Integer, ActionItem> getItemMap(InventoryContext context) {
    if (viewPredicate.test(context.getViewerID())) {
      return mask.getItemMap(context);
    } else {
      return fallbackMask.getItemMap(context);
    }
  }
}
