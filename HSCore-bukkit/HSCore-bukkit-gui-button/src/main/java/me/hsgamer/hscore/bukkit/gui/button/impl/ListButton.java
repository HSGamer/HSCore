package me.hsgamer.hscore.bukkit.gui.button.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * The button with a list of child buttons
 */
public class ListButton implements Button {
  private final List<Button> buttons = new ArrayList<>();
  private final Map<UUID, Integer> currentIndexMap = new HashMap<>();
  private boolean keepCurrentIndex = false;

  /**
   * Create a new button
   *
   * @param buttons the child buttons
   */
  public ListButton(List<Button> buttons) {
    this.buttons.addAll(buttons);
  }

  /**
   * Create a new button
   *
   * @param buttons the child buttons
   */
  public ListButton(Button... buttons) {
    this(Arrays.asList(buttons));
  }

  @Override
  public ItemStack getItemStack(UUID uuid) {
    if (keepCurrentIndex && currentIndexMap.containsKey(uuid)) {
      return buttons.get(currentIndexMap.get(uuid)).getItemStack(uuid);
    }

    for (int i = 0; i < buttons.size(); i++) {
      Button button = buttons.get(i);
      ItemStack item = button.getItemStack(uuid);
      if (item != null || button.forceSetAction(uuid)) {
        currentIndexMap.put(uuid, i);
        return item;
      }
    }

    return null;
  }

  @Override
  public void handleAction(UUID uuid, InventoryClickEvent event) {
    Optional.ofNullable(currentIndexMap.get(uuid))
      .map(buttons::get)
      .ifPresent(button -> button.handleAction(uuid, event));
  }

  @Override
  public boolean forceSetAction(UUID uuid) {
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
   * Should the button keep the current index for the unique id on every {@link #getItemStack(UUID)} times?
   *
   * @return true if it should
   */
  public boolean isKeepCurrentIndex() {
    return keepCurrentIndex;
  }

  /**
   * Should the button keep the current index for the unique id on every {@link #getItemStack(UUID)} times?
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
