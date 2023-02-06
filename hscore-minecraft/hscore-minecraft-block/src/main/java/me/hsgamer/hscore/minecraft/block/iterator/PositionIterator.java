package me.hsgamer.hscore.minecraft.block.iterator;

import me.hsgamer.hscore.minecraft.block.box.Position;

import java.util.Iterator;

/**
 * The {@link Iterator} for {@link Position}
 */
public interface PositionIterator extends Iterator<Position> {
  /**
   * Reset the iterator
   */
  void reset();
}
