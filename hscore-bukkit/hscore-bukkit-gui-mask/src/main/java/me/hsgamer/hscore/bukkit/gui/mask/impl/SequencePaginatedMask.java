package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;

import java.util.*;

/**
 * A button paginated mask, where each {@link Button} is a page
 */
public abstract class SequencePaginatedMask extends PaginatedMask {
  protected final List<Integer> slots = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param name  the name of the mask
   * @param slots the slots
   */
  protected SequencePaginatedMask(String name, List<Integer> slots) {
    super(name);
    this.slots.addAll(slots);
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
   * Get the buttons for the unique id
   *
   * @param uuid the unique id
   *
   * @return the buttons
   */
  public abstract List<Button> getButtons(UUID uuid);

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    List<Button> buttons = getButtons(uuid);
    if (buttons.isEmpty() || this.slots.isEmpty()) {
      return Collections.emptyMap();
    }

    Map<Integer, Button> map = new HashMap<>();
    int basePage = this.getPage(uuid);
    int buttonsSize = buttons.size();
    int slotsSize = this.slots.size();

    for (int i = 0; i < slotsSize; i++) {
      int index = i + basePage;
      if (this.cycle) {
        index = this.getExactPage(index, uuid);
      } else if (index >= buttonsSize) {
        break;
      }
      map.put(this.slots.get(i), buttons.get(index));
    }
    return map;
  }

  @Override
  protected int getPageAmount(UUID uuid) {
    return getButtons(uuid).size();
  }

  @Override
  public void stop() {
    this.slots.clear();
    this.pageNumberMap.clear();
  }
}
