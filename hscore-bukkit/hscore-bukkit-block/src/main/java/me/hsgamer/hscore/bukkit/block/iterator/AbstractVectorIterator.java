package me.hsgamer.hscore.bukkit.block.iterator;

import me.hsgamer.hscore.bukkit.block.box.BlockBox;

/**
 * The abstract {@link VectorIterator} for {@link BlockBox}
 */
public abstract class AbstractVectorIterator implements VectorIterator {
  /**
   * The box
   */
  public final BlockBox box;

  /**
   * Create a new {@link AbstractVectorIterator}
   *
   * @param box the box
   */
  protected AbstractVectorIterator(BlockBox box) {
    this.box = box;
  }
}
