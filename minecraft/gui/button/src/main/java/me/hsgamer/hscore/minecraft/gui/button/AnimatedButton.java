package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.animate.Animation;
import me.hsgamer.hscore.minecraft.gui.common.button.Button;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The animated button with child buttons as frames
 */
public class AnimatedButton implements Button {
  private final List<Button> buttons = new ArrayList<>();
  private final Map<UUID, Animation<Button>> animationMap = new ConcurrentHashMap<>();
  private long periodMillis = 50L;

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
   * Add button(s)
   *
   * @param buttons the buttons (or frames)
   * @param <T>     the type of the button
   */
  public <T extends Button> void addButton(@NotNull Collection<@NotNull T> buttons) {
    this.buttons.addAll(buttons);
  }

  /**
   * Add button(s)
   *
   * @param button the button (or frame)
   */
  public void addButton(@NotNull Button... button) {
    addButton(Arrays.asList(button));
  }

  /**
   * Get the list of buttons
   *
   * @return the buttons
   */
  public List<Button> getButtons() {
    return buttons;
  }

  private Animation<Button> getAnimation(UUID uuid) {
    return animationMap.computeIfAbsent(uuid, key -> new Animation<>(buttons, periodMillis));
  }

  @Override
  public @Nullable ActionItem getItem(@NotNull InventoryContext context) {
    return getAnimation(context.getViewerID()).getCurrentFrame().getItem(context);
  }

  @Override
  public void init() {
    if (this.buttons.isEmpty()) {
      throw new IllegalArgumentException("There is no child button for this animated button");
    }
    this.buttons.forEach(Button::init);
  }

  @Override
  public void stop() {
    this.animationMap.clear();
    this.buttons.forEach(Button::stop);
  }
}
