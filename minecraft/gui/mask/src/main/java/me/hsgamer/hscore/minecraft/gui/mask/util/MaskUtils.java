package me.hsgamer.hscore.minecraft.gui.mask.util;

import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryPosition;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The utility class for masks
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
  public static @NotNull Stream<InventoryPosition> generateAreaPositions(@NotNull InventoryPosition position1, @NotNull InventoryPosition position2) {
    InventoryPosition max = InventoryPosition.maxPosition(position1, position2);
    InventoryPosition min = InventoryPosition.minPosition(position1, position2);
    return IntStream.rangeClosed(min.getY(), max.getY())
      .mapToObj(y -> IntStream.rangeClosed(min.getX(), max.getX()).mapToObj(x -> InventoryPosition.of(x, y)))
      .flatMap(Function.identity());
  }

  /**
   * Get the stream of positions drawing the outline of the area between 2 positions
   *
   * @param position1 the first position
   * @param position2 the second position
   *
   * @return the stream of positions
   */
  public static @NotNull Stream<InventoryPosition> generateOutlinePositions(@NotNull InventoryPosition position1, @NotNull InventoryPosition position2) {
    InventoryPosition max = InventoryPosition.maxPosition(position1, position2);
    InventoryPosition min = InventoryPosition.minPosition(position1, position2);
    Stream<InventoryPosition> top = IntStream.rangeClosed(min.getX(), max.getX()).mapToObj(x -> InventoryPosition.of(x, min.getY()));
    Stream<InventoryPosition> right = IntStream.rangeClosed(min.getY(), max.getY()).mapToObj(y -> InventoryPosition.of(max.getX(), y));
    Stream<InventoryPosition> bottom = IntStream.rangeClosed(min.getX(), max.getX()).mapToObj(x -> InventoryPosition.of(x, max.getY()));
    Stream<InventoryPosition> left = IntStream.rangeClosed(min.getY(), max.getY()).mapToObj(y -> InventoryPosition.of(min.getX(), y));
    return Stream.concat(Stream.concat(top, right), Stream.concat(bottom, left)).distinct();
  }

  /**
   * Create a mask slot from the positions
   *
   * @param positions the positions
   *
   * @return the mask slot
   */
  public static @NotNull Function<InventoryContext, List<Integer>> createPositionMaskSlot(@NotNull List<InventoryPosition> positions) {
    return context -> positions.stream().map(context::getSlot).collect(Collectors.toList());
  }

  /**
   * Create a mask slot from the positions
   *
   * @param positions the positions
   *
   * @return the mask slot
   */
  public static @NotNull Function<InventoryContext, List<Integer>> createPositionMaskSlot(@NotNull InventoryPosition... positions) {
    return MaskUtils.createPositionMaskSlot(Arrays.asList(positions));
  }

  /**
   * Create a static mask slot
   *
   * @param slots the slots
   *
   * @return the mask slot
   */
  public static @NotNull Function<InventoryContext, List<Integer>> createStaticMaskSlot(@NotNull List<Integer> slots) {
    return context -> slots;
  }

  /**
   * Create a static mask slot
   *
   * @param slots the slots
   *
   * @return the mask slot
   */
  public static @NotNull Function<InventoryContext, List<Integer>> createStaticMaskSlot(@NotNull Integer... slots) {
    return createStaticMaskSlot(Arrays.asList(slots));
  }

  /**
   * Create a background mask slot
   *
   * @return the mask slot
   */
  public static @NotNull Function<InventoryContext, List<Integer>> createBackgroundMaskSlot() {
    return context -> IntStream.range(0, context.getSize()).boxed().collect(Collectors.toList());
  }
}
