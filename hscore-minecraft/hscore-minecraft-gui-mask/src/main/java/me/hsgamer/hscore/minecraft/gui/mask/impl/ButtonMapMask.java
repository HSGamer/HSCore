package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import org.jetbrains.annotations.NotNull;

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
  public ButtonMapMask(@NotNull String name) {
    super(name);
  }

  /**
   * Create a new mask
   *
   * @param name      the name of the mask
   * @param buttonMap the map of slot and button
   */
  public ButtonMapMask(@NotNull String name, @NotNull Map<@NotNull Button, @NotNull List<@NotNull Integer>> buttonMap) {
    super(name);
    buttonMap.forEach(this::addButton);
  }

  /**
   * Add a button to the mask
   *
   * @param button the button
   * @param slots  the slots
   */
  public void addButton(@NotNull Button button, @NotNull List<@NotNull Integer> slots) {
    buttons.add(button);
    slots.forEach(slot -> buttonMap.put(slot, button));
  }

  /**
   * Add a button to the mask
   *
   * @param button the button
   * @param slots  the slots
   */
  public void addButton(@NotNull Button button, @NotNull Integer... slots) {
    addButton(button, Arrays.asList(slots));
  }

  /**
   * Get the buttons
   *
   * @return the buttons
   */
  @NotNull
  public List<Button> getButtons() {
    return Collections.unmodifiableList(buttons);
  }

  /**
   * Get the button map
   *
   * @return the button map
   */
  @NotNull
  public Map<Integer, Button> getButtonMap() {
    return Collections.unmodifiableMap(buttonMap);
  }

  @Override
  public @NotNull Map<Integer, Button> generateButtons(@NotNull UUID uuid) {
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
