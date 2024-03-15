package me.hsgamer.hscore.minecraft.gui.button.impl;

import me.hsgamer.hscore.minecraft.gui.GUIProperties;
import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.button.DisplayButton;
import me.hsgamer.hscore.ui.property.IdentifiedUpdatable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The animated button with child buttons as frames
 */
public class AnimatedButton implements Button, IdentifiedUpdatable {
  private final List<Button> buttons = new ArrayList<>();
  private final Map<UUID, Integer> currentIndexMap = new ConcurrentHashMap<>();
  private final Map<UUID, Long> lastUpdateMap = new ConcurrentHashMap<>();
  private long periodMillis = 50L;

  /**
   * Set the period of the animation
   *
   * @param periodMillis the period in milliseconds
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public AnimatedButton setPeriodMillis(long periodMillis) {
    if (periodMillis <= 0) {
      throw new IllegalArgumentException("Period must be positive");
    }
    this.periodMillis = periodMillis;
    return this;
  }

  /**
   * Set the period of the animation
   *
   * @param periodTicks the period in ticks
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public AnimatedButton setPeriodTicks(long periodTicks) {
    return setPeriodMillis(Math.max(periodTicks, 1) * GUIProperties.getMillisPerTick());
  }

  /**
   * Add button(s)
   *
   * @param buttons the buttons (or frames)
   * @param <T>     the type of the button
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public <T extends Button> AnimatedButton addButton(@NotNull Collection<@NotNull T> buttons) {
    this.buttons.addAll(buttons);
    return this;
  }

  /**
   * Add button(s)
   *
   * @param button the button (or frame)
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public AnimatedButton addButton(@NotNull Button... button) {
    return addButton(Arrays.asList(button));
  }

  /**
   * Get the list of buttons
   *
   * @return the buttons
   */
  public List<Button> getButtons() {
    return buttons;
  }

  private int getCurrentIndex(UUID uuid) {
    return currentIndexMap.getOrDefault(uuid, 0);
  }

  @Override
  public DisplayButton view(@NotNull UUID uuid) {
    update(uuid);
    return buttons.get(getCurrentIndex(uuid)).view(uuid);
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
    this.buttons.forEach(Button::stop);
  }

  @Override
  public void update(@NotNull UUID uuid) {
    long currentTimeMillis = System.currentTimeMillis();
    long lastUpdate = lastUpdateMap.computeIfAbsent(uuid, k -> currentTimeMillis);
    if (currentTimeMillis - lastUpdate < periodMillis) return;
    long diff = currentTimeMillis - lastUpdate;
    long remainder = diff % periodMillis;
    int skip = (int) (diff / periodMillis);
    int currentIndex = getCurrentIndex(uuid);
    currentIndexMap.put(uuid, (currentIndex + skip) % buttons.size());
    lastUpdateMap.put(uuid, currentTimeMillis - remainder);
  }
}
