package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.animate.Animation;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * The animated mask with child masks as frames
 */
public class AnimatedMask extends MultiMask<Map<Integer, ActionItem>> {
  private final Map<UUID, Animation<Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>>>> animationMap = new ConcurrentHashMap<>();
  private long periodMillis = 50;

  @Override
  protected boolean requireChildElements() {
    return true;
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

  private Animation<Function<@NotNull InventoryContext, @Nullable Map<Integer, ActionItem>>> getAnimation(@NotNull UUID uuid) {
    return animationMap.computeIfAbsent(uuid, k -> new Animation<>(elements, periodMillis));
  }

  @Override
  public void stop() {
    this.animationMap.clear();
    super.stop();
  }

  @Override
  public @Nullable Map<Integer, ActionItem> apply(@NotNull InventoryContext context) {
    return getAnimation(context.getViewerID()).getCurrentFrame().apply(context);
  }
}
