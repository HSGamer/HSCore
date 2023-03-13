package me.hsgamer.hscore.minecraft.block.utils;

import me.hsgamer.hscore.collections.map.CaseInsensitiveStringHashMap;
import me.hsgamer.hscore.minecraft.block.box.BlockBox;
import me.hsgamer.hscore.minecraft.block.impl.iterator.LinearPositionIterator;
import me.hsgamer.hscore.minecraft.block.impl.iterator.RandomPositionIterator;
import me.hsgamer.hscore.minecraft.block.impl.iterator.RandomTypePositionIterator;
import me.hsgamer.hscore.minecraft.block.iterator.PositionIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import static me.hsgamer.hscore.minecraft.block.impl.iterator.LinearPositionIterator.*;

/**
 * The utility class for {@link PositionIterator}
 */
public final class PositionIteratorUtils {
  private static final Map<String, Function<BlockBox, PositionIterator>> TYPE_MAP = new CaseInsensitiveStringHashMap<>();

  static {
    registerType("random", RandomPositionIterator::new);
    registerType("xyz", box -> new LinearPositionIterator(box, X_COORDINATE, Y_COORDINATE, Z_COORDINATE));
    registerType("xzy", box -> new LinearPositionIterator(box, X_COORDINATE, Z_COORDINATE, Y_COORDINATE));
    registerType("yxz", box -> new LinearPositionIterator(box, Y_COORDINATE, X_COORDINATE, Z_COORDINATE));
    registerType("yzx", box -> new LinearPositionIterator(box, Y_COORDINATE, Z_COORDINATE, X_COORDINATE));
    registerType("zxy", box -> new LinearPositionIterator(box, Z_COORDINATE, X_COORDINATE, Y_COORDINATE));
    registerType("zyx", box -> new LinearPositionIterator(box, Z_COORDINATE, Y_COORDINATE, X_COORDINATE));
    registerType("default", LinearPositionIterator::new);
  }

  private PositionIteratorUtils() {
    // EMPTY
  }

  /**
   * Register a {@link PositionIterator} type
   *
   * @param type         the type
   * @param typeFunction the function to create the {@link PositionIterator}
   */
  public static void registerType(String type, Function<BlockBox, PositionIterator> typeFunction) {
    TYPE_MAP.put(type, typeFunction);
  }

  /**
   * Create a {@link RandomTypePositionIterator} for the {@link BlockBox}
   *
   * @param box the {@link BlockBox}
   *
   * @return the {@link PositionIterator}
   */
  public static PositionIterator random(BlockBox box) {
    return new RandomTypePositionIterator(box, new ArrayList<>(TYPE_MAP.values()));
  }

  /**
   * Get the {@link PositionIterator} by the type
   *
   * @param type the type
   * @param box  the {@link BlockBox}
   *
   * @return the {@link PositionIterator}
   */
  public static PositionIterator get(String type, BlockBox box) {
    return TYPE_MAP.getOrDefault(type, PositionIteratorUtils::random).apply(box);
  }

  /**
   * Get the registered types
   *
   * @return the registered types
   */
  public Collection<String> getRegisteredTypes() {
    return Collections.unmodifiableSet(TYPE_MAP.keySet());
  }
}
