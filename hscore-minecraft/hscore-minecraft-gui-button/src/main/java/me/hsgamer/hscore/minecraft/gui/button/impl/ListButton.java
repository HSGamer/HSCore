package me.hsgamer.hscore.minecraft.gui.button.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The button with a list of child buttons
 */
public class ListButton implements Button {
  private final List<Button> buttons = new ArrayList<>();
  private final Map<UUID, Integer> currentIndexMap = new ConcurrentHashMap<>();
  private boolean keepCurrentIndex = false;

  /**
   * Create a new button
   *
   * @param buttons the child buttons
   */
  public ListButton(@NotNull List<@NotNull Button> buttons) {
    this.buttons.addAll(buttons);
  }

  /**
   * Create a new button
   *
   * @param buttons the child buttons
   */
  public ListButton(@NotNull Button... buttons) {
    this(Arrays.asList(buttons));
  }

  @Override
  public Item getItem(@NotNull UUID uuid) {
    if (keepCurrentIndex && currentIndexMap.containsKey(uuid)) {
      return buttons.get(currentIndexMap.get(uuid)).getItem(uuid);
    }

    for (int i = 0; i < buttons.size(); i++) {
      Button button = buttons.get(i);
      Item item = button.getItem(uuid);
      if (item != null || button.forceSetAction(uuid)) {
        currentIndexMap.put(uuid, i);
        return item;
      }
    }

    return null;
  }

  @Override
  public void handleAction(ClickEvent event) {
    Optional.ofNullable(currentIndexMap.get(event.getViewerID()))
      .map(buttons::get)
      .ifPresent(button -> button.handleAction(event));
  }

  @Override
  public boolean forceSetAction(@NotNull UUID uuid) {
    return Optional.ofNullable(currentIndexMap.get(uuid))
      .map(buttons::get)
      .map(button -> button.forceSetAction(uuid))
      .orElse(false);
  }

  @Override
  public void init() {
    this.buttons.forEach(Button::init);
  }

  @Override
  public void stop() {
    this.buttons.forEach(Button::stop);
  }

  /**
   * Remove the current index for the unique id
   *
   * @param uuid the unique id
   */
  public void removeCurrentIndex(UUID uuid) {
    this.currentIndexMap.remove(uuid);
  }

  /**
   * Should the button keep the current index for the unique id on every {@link #getItem(UUID)} times?
   *
   * @return true if it should
   */
  public boolean isKeepCurrentIndex() {
    return keepCurrentIndex;
  }

  /**
   * Should the button keep the current index for the unique id on every {@link #getItem(UUID)} times?
   *
   * @param keepCurrentIndex true if it should
   */
  public void setKeepCurrentIndex(boolean keepCurrentIndex) {
    this.keepCurrentIndex = keepCurrentIndex;
  }

  /**
   * Get the list of buttons
   *
   * @return the buttons
   */
  public List<Button> getButtons() {
    return buttons;
  }
}
