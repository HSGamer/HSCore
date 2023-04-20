package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.ui.Position2D;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
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
   * @param x          the x position
   * @param y          the y position
   * @param slotPerRow the number of slots for each row
   *
   * @return the raw slot
   */
  public static int toSlot(int x, int y, int slotPerRow) {
    return x + y * slotPerRow;
  }

  /**
   * Get the raw slot from the position, assuming the number of slots for each row is 9
   *
   * @param x the x position
   * @param y the y position
   *
   * @return the raw slot
   */
  public static int toSlot(int x, int y) {
    return toSlot(x, y, 9);
  }

  /**
   * Convert the slot to the position
   *
   * @param slot       the slot
   * @param slotPerRow the number of slots for each row
   *
   * @return the position
   */
  @NotNull
  public static Position2D toPosition(int slot, int slotPerRow) {
    int x = slot % slotPerRow;
    int y = slot / slotPerRow;
    return Position2D.of(x, y);
  }

  /**
   * Convert the slot to the position, assuming the number of slots for each row is 9
   *
   * @param slot the slot
   *
   * @return the position
   */
  @NotNull
  public static Position2D toPosition(int slot) {
    return toPosition(slot, 9);
  }

  /**
   * Get the raw slot from the position
   *
   * @param position   the pair value for the position
   * @param slotPerRow the number of slots for each row
   *
   * @return the raw slot
   */
  public static int toSlot(@NotNull Position2D position, int slotPerRow) {
    return toSlot(position.getX(), position.getY(), slotPerRow);
  }

  /**
   * Get the raw slot from the position, assuming the number of slots for each row is 9
   *
   * @param position the pair value for the position
   *
   * @return the raw slot
   */
  public static int toSlot(@NotNull Position2D position) {
    return toSlot(position, 9);
  }

  /**
   * Generate the stream of slots in the area between two positions
   *
   * @param x1         the x of the first position
   * @param y1         the y of the first position
   * @param x2         the x of the second position
   * @param y2         the y of the second position
   * @param slotPerRow the number of slots for each row
   *
   * @return the stream of slots
   */
  public static IntStream generateAreaSlots(int x1, int y1, int x2, int y2, int slotPerRow) {
    Position2D max = Position2D.maxPosition(x1, y1, x2, y2);
    Position2D min = Position2D.minPosition(x1, y1, x2, y2);
    return IntStream.rangeClosed(min.getY(), max.getY()).flatMap(y -> IntStream.rangeClosed(toSlot(min.getX(), y, slotPerRow), toSlot(max.getX(), y, slotPerRow)));
  }

  /**
   * Generate the stream of slots in the area between two positions, assuming the number of slots for each row is 9
   *
   * @param x1 the x of the first position
   * @param y1 the y of the first position
   * @param x2 the x of the second position
   * @param y2 the y of the second position
   *
   * @return the stream of slots
   */
  public static IntStream generateAreaSlots(int x1, int y1, int x2, int y2) {
    return generateAreaSlots(x1, y1, x2, y2, 9);
  }

  /**
   * Generate the stream of slots in the area between two positions
   *
   * @param position1  the first position
   * @param position2  the second position
   * @param slotPerRow the number of slots for each row
   *
   * @return the stream of slots
   */
  @NotNull
  public static IntStream generateAreaSlots(@NotNull Position2D position1, @NotNull Position2D position2, int slotPerRow) {
    return generateAreaSlots(position1.getX(), position1.getY(), position2.getX(), position2.getY(), slotPerRow);
  }

  /**
   * Generate the stream of slots in the area between two positions, assuming the number of slots for each row is 9
   *
   * @param position1 the first position
   * @param position2 the second position
   *
   * @return the stream of slots
   */
  @NotNull
  public static IntStream generateAreaSlots(@NotNull Position2D position1, @NotNull Position2D position2) {
    return generateAreaSlots(position1, position2, 9);
  }

  /**
   * Get the stream of slots drawing the outline of the area between 2 positions
   *
   * @param x1         the x of the first position
   * @param y1         the y of the first position
   * @param x2         the x of the second position
   * @param y2         the y of the second position
   * @param slotPerRow the number of slots for each row
   *
   * @return the stream of slots
   */
  @NotNull
  public static IntStream generateOutlineSlots(int x1, int y1, int x2, int y2, int slotPerRow) {
    Position2D max = Position2D.maxPosition(x1, y1, x2, y2);
    Position2D min = Position2D.minPosition(x1, y1, x2, y2);
    IntStream top = IntStream.rangeClosed(toSlot(min.getX(), min.getY(), slotPerRow), toSlot(max.getX(), min.getY(), slotPerRow));
    IntStream right = IntStream.rangeClosed(min.getY(), max.getY()).map(y -> toSlot(max.getX(), y, slotPerRow));
    IntStream bottom = IntStream.rangeClosed(toSlot(min.getX(), max.getY(), slotPerRow), toSlot(max.getX(), max.getY(), slotPerRow)).boxed().sorted(Collections.reverseOrder()).mapToInt(Integer::intValue);
    IntStream left = IntStream.rangeClosed(min.getY(), max.getY()).map(y -> toSlot(min.getX(), y, slotPerRow)).boxed().sorted(Collections.reverseOrder()).mapToInt(Integer::intValue);
    return IntStream.concat(top, IntStream.concat(right, IntStream.concat(bottom, left))).distinct();
  }

  /**
   * Get the stream of slots drawing the outline of the area between 2 positions, assuming the number of slots for each row is 9
   *
   * @param x1 the x of the first position
   * @param y1 the y of the first position
   * @param x2 the x of the second position
   * @param y2 the y of the second position
   *
   * @return the stream of slots
   */
  @NotNull
  public static IntStream generateOutlineSlots(int x1, int y1, int x2, int y2) {
    return generateOutlineSlots(x1, y1, x2, y2, 9);
  }

  /**
   * Get the stream of slots drawing the outline of the area between 2 positions
   *
   * @param position1  the first position
   * @param position2  the second position
   * @param slotPerRow the number of slots for each row
   *
   * @return the stream of slots
   */
  @NotNull
  public static IntStream generateOutlineSlots(Position2D position1, Position2D position2, int slotPerRow) {
    return generateOutlineSlots(position1.getX(), position1.getY(), position2.getX(), position2.getY(), slotPerRow);
  }

  /**
   * Get the stream of slots drawing the outline of the area between 2 positions, assuming the number of slots for each row is 9
   *
   * @param position1 the first position
   * @param position2 the second position
   *
   * @return the stream of slots
   */
  @NotNull
  public static IntStream generateOutlineSlots(Position2D position1, Position2D position2) {
    return generateOutlineSlots(position1, position2, 9);
  }
}
