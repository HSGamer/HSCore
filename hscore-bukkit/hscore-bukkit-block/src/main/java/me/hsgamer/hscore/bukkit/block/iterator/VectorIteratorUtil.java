package me.hsgamer.hscore.bukkit.block.iterator;

import me.hsgamer.hscore.bukkit.block.box.BlockBox;
import me.hsgamer.hscore.bukkit.block.iterator.impl.LinearVectorIterator;
import me.hsgamer.hscore.bukkit.block.iterator.impl.RandomTypeVectorIterator;
import me.hsgamer.hscore.bukkit.block.iterator.impl.RandomVectorIterator;
import me.hsgamer.hscore.collections.map.CaseInsensitiveStringHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import static me.hsgamer.hscore.bukkit.block.iterator.impl.LinearVectorIterator.*;

/**
 * The utility class for {@link VectorIterator}
 */
public final class VectorIteratorUtil {
  private static final Map<String, Function<BlockBox, VectorIterator>> TYPE_MAP = new CaseInsensitiveStringHashMap<>();

  static {
    registerType("random", RandomVectorIterator::new);
    registerType("xyz", box -> new LinearVectorIterator(box, X_COORDINATE, Y_COORDINATE, Z_COORDINATE));
    registerType("xzy", box -> new LinearVectorIterator(box, X_COORDINATE, Z_COORDINATE, Y_COORDINATE));
    registerType("yxz", box -> new LinearVectorIterator(box, Y_COORDINATE, X_COORDINATE, Z_COORDINATE));
    registerType("yzx", box -> new LinearVectorIterator(box, Y_COORDINATE, Z_COORDINATE, X_COORDINATE));
    registerType("zxy", box -> new LinearVectorIterator(box, Z_COORDINATE, X_COORDINATE, Y_COORDINATE));
    registerType("zyx", box -> new LinearVectorIterator(box, Z_COORDINATE, Y_COORDINATE, X_COORDINATE));
    registerType("default", LinearVectorIterator::new);
  }

  private VectorIteratorUtil() {
    // EMPTY
  }

  /**
   * Register a {@link VectorIterator} type
   *
   * @param type         the type
   * @param typeFunction the function to create the {@link VectorIterator}
   */
  public static void registerType(String type, Function<BlockBox, VectorIterator> typeFunction) {
    TYPE_MAP.put(type, typeFunction);
  }

  /**
   * Create a {@link RandomTypeVectorIterator} for the {@link BlockBox}
   *
   * @param box the {@link BlockBox}
   *
   * @return the {@link VectorIterator}
   */
  public static VectorIterator random(BlockBox box) {
    return new RandomTypeVectorIterator(box, new ArrayList<>(TYPE_MAP.values()));
  }

  /**
   * Get the {@link VectorIterator} by the type
   *
   * @param type the type
   * @param box  the {@link BlockBox}
   *
   * @return the {@link VectorIterator}
   */
  public static VectorIterator get(String type, BlockBox box) {
    return TYPE_MAP.getOrDefault(type, VectorIteratorUtil::random).apply(box);
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