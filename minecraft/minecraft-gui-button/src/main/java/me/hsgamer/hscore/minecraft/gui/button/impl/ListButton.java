package me.hsgamer.hscore.minecraft.gui.button.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.button.DisplayButton;
import org.jetbrains.annotations.Contract;
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
   * Add button(s)
   *
   * @param buttons the buttons
   * @param <T>     the type of the button
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public <T extends Button> ListButton addButton(@NotNull Collection<@NotNull T> buttons) {
    this.buttons.addAll(buttons);
    return this;
  }

  /**
   * Add button(s)
   *
   * @param button the button
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public ListButton addButton(@NotNull Button... button) {
    return addButton(Arrays.asList(button));
  }

  /**
   * Should the button keep the current index for the unique id on every {@link #display(UUID)} times?
   *
   * @return true if it should
   */
  public boolean isKeepCurrentIndex() {
    return keepCurrentIndex;
  }

  /**
   * Should the button keep the current index for the unique id on every {@link #display(UUID)} times?
   *
   * @param keepCurrentIndex true if it should
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public ListButton setKeepCurrentIndex(boolean keepCurrentIndex) {
    this.keepCurrentIndex = keepCurrentIndex;
    return this;
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
   * Get the list of buttons
   *
   * @return the buttons
   */
  public List<Button> getButtons() {
    return buttons;
  }

  @Override
  public void init() {
    this.buttons.forEach(Button::init);
  }

  @Override
  public void stop() {
    this.buttons.forEach(Button::stop);
  }

  @Override
  public DisplayButton display(@NotNull UUID uuid) {
    if (keepCurrentIndex && currentIndexMap.containsKey(uuid)) {
      return buttons.get(currentIndexMap.get(uuid)).display(uuid);
    }

    for (int i = 0; i < buttons.size(); i++) {
      Button button = buttons.get(i);
      DisplayButton item = button.display(uuid);
      if (item != null) {
        currentIndexMap.put(uuid, i);
        return item;
      }
    }

    return null;
  }
}
