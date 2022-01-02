package me.hsgamer.hscore.bukkit.gui.mask;

import me.hsgamer.hscore.ui.Position2D;

import java.util.stream.IntStream;

/**
 * The utility class for {@link Mask}
 */
public class MaskUtils {

  private MaskUtils() {
    // EMPTY
  }

  /**
   * Get the raw slot from the position
   *
   * @param x the x position
   * @param y the y position
   *
   * @return the raw slot
   */
  public static int toSlot(int x, int y) {
    return x + y * 9;
  }

  /**
   * Convert the slot to the position
   *
   * @param slot the slot
   *
   * @return the position
   */
  public static Position2D toPosition(int slot) {
    int x = slot % 9;
    int y = slot / 9;
    return Position2D.of(x, y);
  }

  /**
   * Get the raw slot from the position
   *
   * @param position the pair value for the position
   *
   * @return the raw slot
   */
  public static int toSlot(Position2D position) {
    return toSlot(position.getX(), position.getY());
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
  public static IntStream generateAreaSlots(int x1, int y1, int x2, int y2) {
    Position2D max = Position2D.maxPosition(x1, y1, x2, y2);
    Position2D min = Position2D.minPosition(x1, y1, x2, y2);
    return IntStream.rangeClosed(min.getY(), max.getY()).flatMap(y -> IntStream.rangeClosed(toSlot(min.getX(), y), toSlot(max.getX(), y)));
  }

  /**
   * Generate the stream of slots in the area between two positions
   *
   * @param position1 the first position
   * @param position2 the second position
   *
   * @return the stream of slots
   */
  public static IntStream generateAreaSlots(Position2D position1, Position2D position2) {
    return generateAreaSlots(position1.getX(), position1.getY(), position2.getX(), position2.getY());
  }

  /**
   * Get the stream of slots drawing the outline of the area between 2 positions
   *
   * @param x1 the x of the first position
   * @param y1 the y of the first position
   * @param x2 the x of the second position
   * @param y2 the y of the second position
   *
   * @return the stream of slots
   */
  public static IntStream generateOutlineSlots(int x1, int y1, int x2, int y2) {
    Position2D max = Position2D.maxPosition(x1, y1, x2, y2);
    Position2D min = Position2D.minPosition(x1, y1, x2, y2);
    return IntStream
      .rangeClosed(min.getY(), max.getY())
      .flatMap(y ->
        y > min.getY() && y < max.getY()
          ? IntStream.of(toSlot(min.getX(), y), toSlot(max.getX(), y))
          : IntStream.rangeClosed(toSlot(min.getX(), y), toSlot(max.getX(), y))
      );
  }

  /**
   * Get the stream of slots drawing the outline of the area between 2 positions
   *
   * @param position1 the first position
   * @param position2 the second position
   *
   * @return the stream of slots
   */
  public static IntStream generateOutlineSlots(Position2D position1, Position2D position2) {
    return generateOutlineSlots(position1.getX(), position1.getY(), position2.getX(), position2.getY());
  }
}
