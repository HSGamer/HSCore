package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.animate.Animation;
import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The animated mask with child masks as frames, but only run once
 */
public class OneTimeAnimatedMask implements ButtonMap {
  private final List<ButtonMap> masks = new ArrayList<>();
  private final Map<UUID, Animation<ButtonMap>> animationMap = new ConcurrentHashMap<>();
  private boolean viewLast = false;
  private long periodMillis = 50L;

  /**
   * Add mask(s)
   *
   * @param masks the mask (or frame)
   * @param <T>   the type of the mask
   */
  public <T extends ButtonMap> void addMask(@NotNull Collection<@NotNull T> masks) {
    this.masks.addAll(masks);
  }

  /**
   * Add mask(s)
   *
   * @param mask the mask (or frame)
   */
  public void addMask(@NotNull ButtonMap... mask) {
    addMask(Arrays.asList(mask));
  }

  /**
   * Set the period of the animation
   *
   * @param periodMillis the period in milliseconds
   */
  public void setPeriodMillis(long periodMillis) {
    if (periodMillis <= 0) {
      throw new IllegalArgumentException("Period must be positive");
    }
    this.periodMillis = periodMillis;
  }

  /**
   * Set whether to view the last frame when the animation is finished
   *
   * @param viewLast true to view the last frame
   */
  public void setViewLast(boolean viewLast) {
    this.viewLast = viewLast;
  }

  /**
   * Get the list of masks
   *
   * @return the masks
   */
  @NotNull
  public List<ButtonMap> getMasks() {
    return masks;
  }

  /**
   * Reset the animation for the unique id
   *
   * @param uuid the unique id
   */
  public void reset(@NotNull UUID uuid) {
    getAnimation(uuid).reset();
  }

  private Animation<ButtonMap> getAnimation(@NotNull UUID uuid) {
    return animationMap.computeIfAbsent(uuid, key -> new Animation<>(masks, periodMillis));
  }

  @Override
  public void init() {
    if (this.masks.isEmpty()) {
      throw new IllegalArgumentException("There is no child mask for this one-time animated mask");
    }
    this.masks.forEach(ButtonMap::init);
  }

  @Override
  public void stop() {
    this.animationMap.clear();
    this.masks.forEach(ButtonMap::stop);
  }

  @Override
  public @Nullable Map<Integer, ActionItem> getItemMap(@NotNull InventoryContext context) {
    Animation<ButtonMap> animation = getAnimation(context.getViewerID());
    long currentMillis = System.currentTimeMillis();
    if (animation.isFirstRun(currentMillis)) {
      return animation.getCurrentFrame(currentMillis).getItemMap(context);
    } else if (viewLast) {
      return masks.get(masks.size() - 1).getItemMap(context);
    } else {
      return null;
    }
  }
}
