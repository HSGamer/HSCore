package me.hsgamer.hscore.bukkit.gui.mask;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.common.Pair;

import java.util.ArrayList;

/**
 * The rectangle mask with two positions
 */
public class RectangleMask extends MultiSlotsMask {
  /**
   * Create a new mask
   *
   * @param name      the name of the mask
   * @param position1 the first position
   * @param position2 the second position
   * @param button    the button
   */
  public RectangleMask(String name, Pair<Integer, Integer> position1, Pair<Integer, Integer> position2, Button button) {
    super(name, MaskUtils.generateAreaSlots(position1, position2).collect(ArrayList::new, ArrayList::add, ArrayList::addAll), button);
  }

  /**
   * Create a new mask
   *
   * @param name   the name of the mask
   * @param slot1  the first slot
   * @param slot2  the second slot
   * @param button the button
   */
  public RectangleMask(String name, int slot1, int slot2, Button button) {
    this(name, MaskUtils.toPosition(slot1), MaskUtils.toPosition(slot2), button);
  }
}
