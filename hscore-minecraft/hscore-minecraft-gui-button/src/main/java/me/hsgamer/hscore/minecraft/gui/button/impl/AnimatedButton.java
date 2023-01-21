package me.hsgamer.hscore.minecraft.gui.button.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import me.hsgamer.hscore.ui.property.IdentifiedUpdatable;
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
  private final long periodMillis;

  /**
   * Create a new button
   *
   * @param periodMillis the delay between each frame
   *                     (in milliseconds)
   * @param buttons      the child buttons (or frames)
   */
  public AnimatedButton(long periodMillis, @NotNull Button... buttons) {
    if (periodMillis <= 0) {
      throw new IllegalArgumentException("Period must be positive");
    }
    this.periodMillis = periodMillis;
    this.buttons.addAll(Arrays.asList(buttons));
  }

  /**
   * Add child buttons
   *
   * @param childButton the child button (or frame)
   */
  public void addChildButtons(Button... childButton) {
    this.buttons.addAll(Arrays.asList(childButton));
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
  public Item getItem(@NotNull UUID uuid) {
    update(uuid);
    return buttons.get(getCurrentIndex(uuid)).getItem(uuid);
  }

  @Override
  public void handleAction(ClickEvent event) {
    buttons.get(getCurrentIndex(event.getViewerID())).handleAction(event);
  }

  @Override
  public boolean forceSetAction(@NotNull UUID uuid) {
    return buttons.get(getCurrentIndex(uuid)).forceSetAction(uuid);
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
    int skip = (int) Math.floor((currentTimeMillis - lastUpdate) / (double) periodMillis);
    int currentIndex = getCurrentIndex(uuid);
    currentIndexMap.put(uuid, (currentIndex + skip) % buttons.size());
    lastUpdateMap.put(uuid, currentTimeMillis);
  }
}
