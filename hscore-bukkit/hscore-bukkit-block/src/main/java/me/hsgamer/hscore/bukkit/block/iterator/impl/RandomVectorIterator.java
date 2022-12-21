package me.hsgamer.hscore.bukkit.block.iterator.impl;

import me.hsgamer.hscore.bukkit.block.box.BlockBox;
import me.hsgamer.hscore.bukkit.block.iterator.BaseVectorIterator;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * The {@link me.hsgamer.hscore.bukkit.block.iterator.VectorIterator} that iterates randomly
 */
public class RandomVectorIterator extends BaseVectorIterator {
  private final LinkedList<Vector> queue;

  /**
   * Create a new {@link RandomVectorIterator}
   *
   * @param box the box
   */
  public RandomVectorIterator(BlockBox box) {
    super(box);
    queue = new LinkedList<>();
  }

  @Override
  public void reset() {
    super.reset();
    queue.clear();
  }

  @Override
  public Vector initial() {
    for (int x = box.minX; x <= box.maxX; x++) {
      for (int y = box.minY; y <= box.maxY; y++) {
        for (int z = box.minZ; z <= box.maxZ; z++) {
          queue.add(new Vector(x, y, z));
        }
      }
    }
    Collections.shuffle(queue);
    return queue.poll();
  }

  @Override
  public Vector getContinue(Vector current) throws NoSuchElementException {
    Vector vector = queue.poll();
    if (vector == null) {
      throw new NoSuchElementException();
    }
    return vector;
  }

  @Override
  public boolean hasContinue(Vector current) {
    return !queue.isEmpty();
  }
}
