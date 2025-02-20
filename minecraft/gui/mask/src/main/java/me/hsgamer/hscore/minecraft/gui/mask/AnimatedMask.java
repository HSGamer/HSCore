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
 * The animated mask with child masks as frames
 */
public class AnimatedMask implements ButtonMap {
  private final List<ButtonMap> masks = new ArrayList<>();
  private final Map<UUID, Animation<ButtonMap>> animationMap = new ConcurrentHashMap<>();
  private long periodMillis = 50;

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
   * Get the list of masks
   *
   * @return the masks
   */
  @NotNull
  public List<ButtonMap> getMasks() {
    return masks;
  }

  private Animation<ButtonMap> getAnimation(@NotNull UUID uuid) {
    return animationMap.computeIfAbsent(uuid, k -> new Animation<>(masks, periodMillis));
  }

  @Override
  public void init() {
    if (this.masks.isEmpty()) {
      throw new IllegalArgumentException("There is no child mask for this animated mask");
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
    return getAnimation(context.getViewerID()).getCurrentFrame().getItemMap(context);
  }
}
