package me.hsgamer.hscore.minecraft.block.impl.iterator;

import me.hsgamer.hscore.minecraft.block.box.BlockBox;
import me.hsgamer.hscore.minecraft.block.box.Position;
import me.hsgamer.hscore.minecraft.block.iterator.BasePositionIterator;
import me.hsgamer.hscore.minecraft.block.iterator.PositionIterator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * The {@link PositionIterator} that iterates randomly
 */
public class RandomPositionIterator extends BasePositionIterator {
  private final LinkedList<Position> queue;

  /**
   * Create a new {@link RandomPositionIterator}
   *
   * @param box the box
   */
  public RandomPositionIterator(BlockBox box) {
    super(box);
    queue = new LinkedList<>();
  }

  @Override
  public void reset() {
    super.reset();
    queue.clear();
  }

  @Override
  public Position initial() {
    for (int x = box.minX; x <= box.maxX; x++) {
      for (int y = box.minY; y <= box.maxY; y++) {
        for (int z = box.minZ; z <= box.maxZ; z++) {
          queue.add(new Position(x, y, z));
        }
      }
    }
    Collections.shuffle(queue);
    return queue.poll();
  }

  @Override
  public Position getContinue(Position current) throws NoSuchElementException {
    Position position = queue.poll();
    if (position == null) {
      throw new NoSuchElementException();
    }
    return position;
  }

  @Override
  public boolean hasContinue(Position current) {
    return !queue.isEmpty();
  }
}
