package me.hsgamer.hscore.minecraft.block.impl.iterator;

import me.hsgamer.hscore.minecraft.block.box.BlockBox;
import me.hsgamer.hscore.minecraft.block.box.Position;
import me.hsgamer.hscore.minecraft.block.iterator.BasePositionIterator;
import me.hsgamer.hscore.minecraft.block.iterator.PositionIterator;

import java.util.NoSuchElementException;

/**
 * The {@link PositionIterator} that iterates in a linear way
 */
public class LinearPositionIterator extends BasePositionIterator {
  /**
   * A linear subject for the x-axis
   */
  public static final LinearCoordinate X_COORDINATE = new LinearCoordinate() {
    @Override
    public boolean hasNext(Position current, BasePositionIterator iterator) {
      return current.x < iterator.box.maxX;
    }

    @Override
    public Position next(Position current) {
      return new Position(current.x + 1, current.y, current.z);
    }

    @Override
    public Position reset(Position current, BasePositionIterator iterator) {
      return new Position(iterator.box.minX, current.y, current.z);
    }
  };
  /**
   * A linear subject for the y-axis
   */
  public static final LinearCoordinate Y_COORDINATE = new LinearCoordinate() {
    @Override
    public boolean hasNext(Position current, BasePositionIterator iterator) {
      return current.y < iterator.box.maxY;
    }

    @Override
    public Position next(Position current) {
      return new Position(current.x, current.y + 1, current.z);
    }

    @Override
    public Position reset(Position current, BasePositionIterator iterator) {
      return new Position(current.x, iterator.box.minY, current.z);
    }
  };
  /**
   * A linear subject for the z-axis
   */
  public static final LinearCoordinate Z_COORDINATE = new LinearCoordinate() {
    @Override
    public boolean hasNext(Position current, BasePositionIterator iterator) {
      return current.z < iterator.box.maxZ;
    }

    @Override
    public Position next(Position current) {
      return new Position(current.x, current.y, current.z + 1);
    }

    @Override
    public Position reset(Position current, BasePositionIterator iterator) {
      return new Position(current.x, current.y, iterator.box.minZ);
    }
  };

  private final LinearCoordinate[] coordinates;

  /**
   * Create a new iterator
   *
   * @param box         the box
   * @param coordinates the linear subjects
   */
  public LinearPositionIterator(BlockBox box, LinearCoordinate... coordinates) {
    super(box);
    this.coordinates = coordinates;
  }

  /**
   * Create a new iterator
   *
   * @param box the box
   */
  public LinearPositionIterator(BlockBox box) {
    this(box, X_COORDINATE, Y_COORDINATE, Z_COORDINATE);
  }

  @Override
  public Position initial() {
    return new Position(box.minX, box.minY, box.minZ);
  }

  @Override
  public Position getContinue(Position current) throws NoSuchElementException {
    Position next = current;
    for (int i = 0; i < coordinates.length; i++) {
      LinearCoordinate coordinate = coordinates[i];
      if (coordinate.hasNext(next, this)) {
        next = coordinate.next(next);
        break;
      } else if (i == coordinates.length - 1) {
        throw new NoSuchElementException("No more elements");
      } else {
        next = coordinate.reset(next, this);
      }
    }
    return next;
  }

  @Override
  public boolean hasContinue(Position current) {
    return current.x < box.maxX || current.y < box.maxY || current.z < box.maxZ;
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
    boolean hasNext(Position current, BasePositionIterator iterator);

    /**
     * Get the next element
     *
     * @param current the current element
     */
    Position next(Position current);

    /**
     * Reset the next element
     *
     * @param current  the current element
     * @param iterator the iterator
     */
    Position reset(Position current, BasePositionIterator iterator);
  }
}
