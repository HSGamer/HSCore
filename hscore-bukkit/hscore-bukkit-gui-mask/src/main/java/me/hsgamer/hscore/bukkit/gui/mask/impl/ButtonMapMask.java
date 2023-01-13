package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.bukkit.gui.mask.BaseMask;

import java.util.*;

/**
 * The mask with a map of slot and button
 */
public class ButtonMapMask extends BaseMask {
  private final List<Button> buttons = new ArrayList<>();
  private final Map<Integer, Button> buttonMap = new HashMap<>();

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  public ButtonMapMask(String name) {
    super(name);
  }

  /**
   * Create a new mask
   *
   * @param name      the name of the mask
   * @param buttonMap the map of slot and button
   */
  public ButtonMapMask(String name, Map<Button, List<Integer>> buttonMap) {
    super(name);
    buttonMap.forEach(this::addButton);
  }

  /**
   * Add a button to the mask
   *
   * @param button the button
   * @param slots  the slots
   */
  public void addButton(Button button, List<Integer> slots) {
    buttons.add(button);
    slots.forEach(slot -> buttonMap.put(slot, button));
  }

  /**
   * Add a button to the mask
   *
   * @param button the button
   * @param slots  the slots
   */
  public void addButton(Button button, Integer... slots) {
    addButton(button, Arrays.asList(slots));
  }

  /**
   * Get the buttons
   *
   * @return the buttons
   */
  public List<Button> getButtons() {
    return Collections.unmodifiableList(buttons);
  }

  /**
   * Get the button map
   *
   * @return the button map
   */
  public Map<Integer, Button> getButtonMap() {
    return Collections.unmodifiableMap(buttonMap);
  }

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    return buttonMap;
  }

  @Override
  public void init() {
    buttons.forEach(Button::init);
  }

  @Override
  public void stop() {
    buttons.forEach(Button::stop);
  }
}
