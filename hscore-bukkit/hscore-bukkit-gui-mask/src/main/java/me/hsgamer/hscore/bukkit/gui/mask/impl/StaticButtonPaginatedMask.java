package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The {@link ButtonPaginatedMask} with the static list of buttons
 */
public class StaticButtonPaginatedMask extends ButtonPaginatedMask {
  protected final List<Button> buttons = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param name    the name of the mask
   * @param slots   the slots
   * @param buttons the buttons
   */
  public StaticButtonPaginatedMask(String name, List<Integer> slots, Button... buttons) {
    super(name, slots);
    this.addButtons(buttons);
  }

  /**
   * Create a new mask
   *
   * @param name    the name of the mask
   * @param slots   the slots
   * @param buttons the buttons
   */
  public StaticButtonPaginatedMask(String name, List<Integer> slots, List<Button> buttons) {
    super(name, slots);
    this.addButtons(buttons);
  }

  /**
   * Add buttons to the mask
   *
   * @param buttons the buttons
   */
  public void addButtons(Button... buttons) {
    this.addButtons(Arrays.asList(buttons));
  }

  /**
   * Add buttons to the mask
   *
   * @param buttons the buttons
   */
  public void addButtons(List<Button> buttons) {
    this.buttons.addAll(buttons);
  }

  @Override
  public List<Button> getButtons(UUID uuid) {
    return this.buttons;
  }

  @Override
  public void init() {
    this.buttons.forEach(Button::init);
  }

  @Override
  public void stop() {
    this.buttons.forEach(Button::stop);
    this.buttons.clear();
    super.stop();
  }
}
