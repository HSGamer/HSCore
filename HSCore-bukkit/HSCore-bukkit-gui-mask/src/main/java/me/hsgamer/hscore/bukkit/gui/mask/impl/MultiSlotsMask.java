package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.bukkit.gui.mask.BaseMask;

import java.util.*;

/**
 * The masks with multiple slots
 */
public class MultiSlotsMask extends BaseMask {
  protected final List<Integer> slots = new ArrayList<>();
  protected final List<Button> buttons = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param name    the name of the mask
   * @param slots   the slots
   * @param buttons the buttons
   */
  public MultiSlotsMask(String name, List<Integer> slots, Button... buttons) {
    super(name);
    this.slots.addAll(slots);
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

  /**
   * Get the slots
   *
   * @return the slots
   */
  public List<Integer> getSlots() {
    return Collections.unmodifiableList(slots);
  }

  /**
   * Get the buttons
   *
   * @return the buttons
   */
  public List<Button> getButtons() {
    return Collections.unmodifiableList(buttons);
  }

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    Map<Integer, Button> map = new HashMap<>();
    if (!this.buttons.isEmpty()) {
      this.slots.forEach(slot -> map.put(slot, this.buttons.get(slot % this.buttons.size())));
    }
    return map;
  }

  @Override
  public void init() {
    if (this.buttons.isEmpty()) {
      return;
    }
    this.buttons.forEach(Button::init);
  }

  @Override
  public void stop() {
    if (this.buttons.isEmpty()) {
      return;
    }
    this.buttons.forEach(Button::stop);
  }
}
