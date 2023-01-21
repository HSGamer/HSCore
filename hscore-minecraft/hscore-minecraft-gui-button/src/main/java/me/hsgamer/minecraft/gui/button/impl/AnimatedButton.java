package me.hsgamer.minecraft.gui.button.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import me.hsgamer.hscore.ui.property.IdentifiedUpdatable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AnimatedButton implements Button, IdentifiedUpdatable {
  private final List<Button> buttons = new ArrayList<>();
  private final Map<UUID, Integer> currentIndexMap = new ConcurrentHashMap<>();
  private final Map<UUID, Long> lastUpdateMap = new ConcurrentHashMap<>();
  private final long periodMillis;

  public AnimatedButton(long periodMillis) {
    if (periodMillis <= 0) {
      throw new IllegalArgumentException("Period must be positive");
    }
    this.periodMillis = periodMillis;
  }

  public AnimatedButton(long periodMillis, @NotNull Button... buttons) {
    this(periodMillis);
    this.buttons.addAll(Arrays.asList(buttons));
  }

  public void addChildButtons(Button... childButton) {
    this.buttons.addAll(Arrays.asList(childButton));
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
  public void update(UUID uuid) {
    long currentTimeMillis = System.currentTimeMillis();
    long lastUpdate = lastUpdateMap.computeIfAbsent(uuid, k -> currentTimeMillis);
    if (currentTimeMillis - lastUpdate < periodMillis) return;
    int skip = (int) Math.floor((currentTimeMillis - lastUpdate) / (double) periodMillis);
    int currentIndex = getCurrentIndex(uuid);
    currentIndexMap.put(uuid, (currentIndex + skip) % buttons.size());
    lastUpdateMap.put(uuid, currentTimeMillis);
  }

  public List<Button> getButtons() {
    return buttons;
  }
}
