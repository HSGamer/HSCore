package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.object.InventoryPosition;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The utility class for {@link Mask}
 */
public class MaskUtils {

  private MaskUtils() {
    // EMPTY
  }

  /**
   * Generate the stream of positions in the area between two positions
   *
   * @param position1 the first position
   * @param position2 the second position
   *
   * @return the stream of positions
   */
  @NotNull
  public static Stream<InventoryPosition> generateAreaPositions(@NotNull InventoryPosition position1, @NotNull InventoryPosition position2) {
    InventoryPosition max = InventoryPosition.maxPosition(position1, position2);
    InventoryPosition min = InventoryPosition.minPosition(position1, position2);
    return IntStream.rangeClosed(min.getY(), max.getY())
      .mapToObj(y -> IntStream.rangeClosed(min.getX(), max.getX()).mapToObj(x -> InventoryPosition.of(x, y)))
      .flatMap(Function.identity());
  }

  /**
   * Generate the stream of slots in the area between two positions
   *
   * @param position1 the first position
   * @param position2 the second position
   * @param size      the size of the inventory
   *
   * @return the stream of slots
   */
  @NotNull
  public static IntStream generateAreaSlots(@NotNull InventoryPosition position1, @NotNull InventoryPosition position2, @NotNull InventorySize size) {
    return size.toSlots(generateAreaPositions(position1, position2));
  }

  /**
   * Get the stream of positions drawing the outline of the area between 2 positions
   *
   * @param position1 the first position
   * @param position2 the second position
   *
   * @return the stream of positions
   */
  @NotNull
  public static Stream<InventoryPosition> generateOutlineSlots(@NotNull InventoryPosition position1, @NotNull InventoryPosition position2) {
    InventoryPosition max = InventoryPosition.maxPosition(position1, position2);
    InventoryPosition min = InventoryPosition.minPosition(position1, position2);
    Stream<InventoryPosition> top = IntStream.rangeClosed(min.getX(), max.getX()).mapToObj(x -> InventoryPosition.of(x, min.getY()));
    Stream<InventoryPosition> right = IntStream.rangeClosed(min.getY(), max.getY()).mapToObj(y -> InventoryPosition.of(max.getX(), y));
    Stream<InventoryPosition> bottom = IntStream.rangeClosed(min.getX(), max.getX()).mapToObj(x -> InventoryPosition.of(x, max.getY()));
    Stream<InventoryPosition> left = IntStream.rangeClosed(min.getY(), max.getY()).mapToObj(y -> InventoryPosition.of(min.getX(), y));
    return Stream.concat(Stream.concat(top, right), Stream.concat(bottom, left)).distinct();
  }

  /**
   * Get the stream of slots drawing the outline of the area between 2 positions
   *
   * @param position1 the first position
   * @param position2 the second position
   * @param size      the size of the inventory
   *
   * @return the stream of slots
   */
  @NotNull
  public static IntStream generateOutlineSlots(@NotNull InventoryPosition position1, @NotNull InventoryPosition position2, @NotNull InventorySize size) {
    return size.toSlots(generateOutlineSlots(position1, position2));
  }
}
