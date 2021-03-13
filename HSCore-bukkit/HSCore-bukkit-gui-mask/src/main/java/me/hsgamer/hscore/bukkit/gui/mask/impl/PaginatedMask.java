package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.bukkit.gui.mask.BaseMask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The paginated mask, those with a long list of {@link Button} divided into pages.
 */
public class PaginatedMask extends BaseMask {
  protected final List<Integer> slots = new ArrayList<>();
  protected final List<Button> buttons = new ArrayList<>();
  protected final Map<UUID, Integer> pageNumberMap = new ConcurrentHashMap<>();

  /**
   * Create a new mask
   *
   * @param name  the name of the mask
   * @param slots the slots
   */
  public PaginatedMask(String name, List<Integer> slots) {
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
  public PaginatedMask(String name, List<Integer> slots, Button... buttons) {
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

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    Map<Integer, Button> map = new HashMap<>();
    int pageNumber = this.pageNumberMap.computeIfAbsent(uuid, uuid1 -> 0);
    for (int i = 0; i < this.slots.size(); i++) {
      int index = i + (pageNumber * this.slots.size());
      if (index >= buttons.size()) {
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

  /**
   * Set the page for the unique id
   *
   * @param uuid the unique id
   * @param page the page
   */
  public void setPage(UUID uuid, int page) {
    if (page <= 0 || this.buttons.size() <= this.slots.size()) {
      page = 0;
    } else if (page * this.slots.size() > this.buttons.size()) {
      page = (int) Math.ceil((double) this.buttons.size() / this.slots.size()) - 1;
    }
    this.pageNumberMap.put(uuid, page);
  }

  /**
   * Set the next page for the unique id
   *
   * @param uuid the unique id
   */
  public void nextPage(UUID uuid) {
    this.setPage(uuid, this.pageNumberMap.computeIfAbsent(uuid, uuid1 -> 0) + 1);
  }

  /**
   * Set the previous page for the unique id
   *
   * @param uuid the unique id
   */
  public void previousPage(UUID uuid) {
    this.setPage(uuid, this.pageNumberMap.computeIfAbsent(uuid, uuid1 -> 0) - 1);
  }
}
