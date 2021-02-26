package me.hsgamer.hscore.bukkit.gui.mask;

import me.hsgamer.hscore.bukkit.gui.button.Button;

import java.util.*;

/**
 * The masks with multiple slots
 */
public class MultiSlotsMask extends BaseMask {
  private final List<Integer> slots = new ArrayList<>();
  private final Button button;

  /**
   * Create a new mask
   *
   * @param name   the name of the mask
   * @param slots  the slots
   * @param button the button
   */
  public MultiSlotsMask(String name, List<Integer> slots, Button button) {
    super(name);
    this.slots.addAll(slots);
    this.button = button;
  }

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    Map<Integer, Button> map = new HashMap<>();
    this.slots.forEach(slot -> map.put(slot, button));
    return map;
  }

  @Override
  public void init() {
    this.button.init();
  }

  @Override
  public void stop() {
    this.button.stop();
  }
}
