package me.hsgamer.hscore.bukkit.gui.mask;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.hscore.ui.property.Initializable;

import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * The interface for all masks
 */
public interface Mask extends Initializable {

  /**
   * Get the raw slot from the position
   *
   * @param x the x position
   * @param y the y position
   *
   * @return the raw slot
   */
  static int generateSlot(int x, int y) {
    return x + y * 9;
  }

  /**
   * Convert the slot to the position
   *
   * @param slot the slot
   *
   * @return the position
   */
  static Pair<Integer, Integer> toPosition(int slot) {
    int x = slot % 9;
    int y = slot / 9;
    return Pair.of(x, y);
  }

  /**
   * Get the raw slot from the position
   *
   * @param position the pair value for the position
   *
   * @return the raw slot
   */
  static int generateSlot(Pair<Integer, Integer> position) {
    return generateSlot(position.getKey(), position.getValue());
  }

  /**
   * Generate the stream of slots in the area between two positions
   *
   * @param x1 the x of the first position
   * @param y1 the y of the first position
   * @param x2 the x of the second position
   * @param y2 the y of the second position
   *
   * @return the stream of slots
   */
  static IntStream generateAreaSlots(int x1, int y1, int x2, int y2) {
    int xMin = Math.min(x1, x2);
    int yMin = Math.min(y1, y2);
    int xMax = Math.max(x1, x2);
    int yMax = Math.max(y1, y2);
    return IntStream.rangeClosed(yMin, yMax).flatMap(y -> IntStream.rangeClosed(generateSlot(xMin, y), generateSlot(xMax, y)));
  }

  /**
   * Generate the stream of slots in the area between two positions
   *
   * @param position1 the pair value for the first position
   * @param position2 the pair value for the second position
   *
   * @return the stream of slots
   */
  static IntStream generateAreaSlots(Pair<Integer, Integer> position1, Pair<Integer, Integer> position2) {
    return generateAreaSlots(position1.getKey(), position1.getValue(), position2.getKey(), position2.getValue());
  }

  /**
   * Generate the buttons for the unique id
   *
   * @param uuid the unique id
   *
   * @return the map contains the slots and the buttons
   */
  Map<Integer, Button> generateButtons(UUID uuid);

  /**
   * Get the name of the mask
   */
  String getName();
}
