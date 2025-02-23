package me.hsgamer.hscore.minecraft.gui.button;

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
 * The animated button with child buttons as frames
 */
public class AnimatedButton extends MultiButton {
  private final Map<UUID, Animation<Function<@NotNull InventoryContext, @Nullable ActionItem>>> animationMap = new ConcurrentHashMap<>();
  private long periodMillis = 50L;

  @Override
  protected boolean requireChildButtons() {
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

  private Animation<Function<@NotNull InventoryContext, @Nullable ActionItem>> getAnimation(UUID uuid) {
    return animationMap.computeIfAbsent(uuid, key -> new Animation<>(buttons, periodMillis));
  }

  @Override
  public void stop() {
    this.animationMap.clear();
    super.stop();
  }

  @Override
  public @Nullable ActionItem apply(@NotNull InventoryContext context) {
    return getAnimation(context.getViewerID()).getCurrentFrame().apply(context);
  }
}
