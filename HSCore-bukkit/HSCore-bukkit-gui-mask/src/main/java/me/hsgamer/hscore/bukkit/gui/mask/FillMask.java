package me.hsgamer.hscore.bukkit.gui.mask;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.common.Pair;

import java.util.*;

public class FillMask extends BaseMask {
  private final List<Integer> slots = new ArrayList<>();
  private final Button button;

  /**
   * Create a new mask
   *
   * @param name      the name of the mask
   * @param position1 the first position
   * @param position2 the second position
   * @param button    the button
   */
  public FillMask(String name, Pair<Integer, Integer> position1, Pair<Integer, Integer> position2, Button button) {
    super(name);
    this.slots.addAll(Mask.generateAreaSlots(position1, position2).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
    this.button = button;
  }

  /**
   * Create a new mask
   *
   * @param name   the name of the mask
   * @param slot1  the first slot
   * @param slot2  the second slot
   * @param button the button
   */
  public FillMask(String name, int slot1, int slot2, Button button) {
    this(name, Mask.toPosition(slot1), Mask.toPosition(slot2), button);
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
