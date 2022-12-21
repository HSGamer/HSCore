package me.hsgamer.hscore.bukkit.block.iterator.impl;

import me.hsgamer.hscore.bukkit.block.box.BlockBox;
import me.hsgamer.hscore.bukkit.block.iterator.BaseVectorIterator;
import org.bukkit.util.Vector;

import java.util.NoSuchElementException;

/**
 * The {@link me.hsgamer.hscore.bukkit.block.iterator.VectorIterator} that iterates in a linear way
 */
public class LinearVectorIterator extends BaseVectorIterator {
  /**
   * A linear subject for the x-axis
   */
  public static final LinearCoordinate X_COORDINATE = new LinearCoordinate() {
    @Override
    public boolean hasNext(Vector current, BaseVectorIterator iterator) {
      return current.getX() < iterator.box.maxX;
    }

    @Override
    public void next(Vector next) {
      next.setX(next.getX() + 1);
    }

    @Override
    public void reset(Vector next, BaseVectorIterator iterator) {
      next.setX(iterator.box.minX);
    }
  };
  /**
   * A linear subject for the y-axis
   */
  public static final LinearCoordinate Y_COORDINATE = new LinearCoordinate() {
    @Override
    public boolean hasNext(Vector current, BaseVectorIterator iterator) {
      return current.getY() < iterator.box.maxY;
    }

    @Override
    public void next(Vector next) {
      next.setY(next.getY() + 1);
    }

    @Override
    public void reset(Vector next, BaseVectorIterator iterator) {
      next.setY(iterator.box.minY);
    }
  };
  /**
   * A linear subject for the z-axis
   */
  public static final LinearCoordinate Z_COORDINATE = new LinearCoordinate() {
    @Override
    public boolean hasNext(Vector current, BaseVectorIterator iterator) {
      return current.getZ() < iterator.box.maxZ;
    }

    @Override
    public void next(Vector next) {
      next.setZ(next.getZ() + 1);
    }

    @Override
    public void reset(Vector next, BaseVectorIterator iterator) {
      next.setZ(iterator.box.minZ);
    }
  };

  private final LinearCoordinate[] coordinates;

  /**
   * Create a new iterator
   *
   * @param box         the box
   * @param coordinates the linear subjects
   */
  public LinearVectorIterator(BlockBox box, LinearCoordinate... coordinates) {
    super(box);
    this.coordinates = coordinates;
  }

  /**
   * Create a new iterator
   *
   * @param box the box
   */
  public LinearVectorIterator(BlockBox box) {
    this(box, X_COORDINATE, Y_COORDINATE, Z_COORDINATE);
  }

  @Override
  public Vector initial() {
    return new Vector(box.minX, box.minY, box.minZ);
  }

  @Override
  public Vector getContinue(Vector current) throws NoSuchElementException {
    Vector next = current.clone();
    for (int i = 0; i < coordinates.length; i++) {
      LinearCoordinate coordinate = coordinates[i];
      if (coordinate.hasNext(next, this)) {
        coordinate.next(next);
        break;
      } else if (i == coordinates.length - 1) {
        throw new NoSuchElementException("No more elements");
      } else {
        coordinate.reset(next, this);
      }
    }
    return next;
  }

  @Override
  public boolean hasContinue(Vector current) {
    return current.getX() < box.maxX || current.getY() < box.maxY || current.getZ() < box.maxZ;
  }

  /**
   * The linear subject
   */
  public interface LinearCoordinate {
    /**
     * Check if there is a next element
     *
     * @param current  the current element
     * @param iterator the iterator
     *
     * @return true if there is a next element
     */
    boolean hasNext(Vector current, BaseVectorIterator iterator);

    /**
     * Get the next element
     *
     * @param next the next element
     */
    void next(Vector next);

    /**
     * Reset the next element
     *
     * @param next     the next element
     * @param iterator the iterator
     */
    void reset(Vector next, BaseVectorIterator iterator);
  }
}
