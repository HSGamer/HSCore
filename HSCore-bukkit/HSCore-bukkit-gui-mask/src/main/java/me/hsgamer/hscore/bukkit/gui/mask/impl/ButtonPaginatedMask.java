package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;

import java.util.*;

/**
 * The button paginated mask, those with a long list of {@link Button} divided into pages.
 */
public class ButtonPaginatedMask extends PaginatedMask {
  protected final List<Integer> slots = new ArrayList<>();
  protected final List<Button> buttons = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param name  the name of the mask
   * @param slots the slots
   */
  public ButtonPaginatedMask(String name, List<Integer> slots) {
    super(name);
    this.slots.addAll(slots);
  }

  /**
   * Create a new mask
   *
   * @param name    the name of the mask
   * @param slots   the slots
   * @param buttons the buttons
   */
  public ButtonPaginatedMask(String name, List<Integer> slots, Button... buttons) {
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

    int pageNumber = this.getPage(uuid);
    int offset = pageNumber * this.slots.size();
    int buttonsSize = this.buttons.size();
    int slotsSize = this.slots.size();

    for (int i = 0; i < slotsSize; i++) {
      int index = i + offset;
      if (index >= buttonsSize) {
        break;
      }
      map.put(this.slots.get(i), this.buttons.get(index));
    }
    return map;
  }

  @Override
  public void init() {
    this.buttons.forEach(Button::init);
  }

  @Override
  public void stop() {
    this.buttons.forEach(Button::stop);
    this.slots.clear();
    this.pageNumberMap.clear();
  }

  @Override
  protected int getMaxPage() {
    return this.buttons.size() <= this.slots.size() ? 0 : (int) Math.ceil((double) this.buttons.size() / this.slots.size()) - 1;
  }
}
