package me.hsgamer.hscore.minecraft.block.iterator;

import me.hsgamer.hscore.minecraft.block.box.BlockBox;
import me.hsgamer.hscore.minecraft.block.box.Position;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The base {@link PositionIterator} for {@link BlockBox}
 */
public abstract class BasePositionIterator extends AbstractPositionIterator {
  private final AtomicReference<Position> current;

  /**
   * Create a new {@link BasePositionIterator}
   *
   * @param box the box
   */
  protected BasePositionIterator(BlockBox box) {
    super(box);
    this.current = new AtomicReference<>();
  }

  /**
   * Get the current {@link Position}
   *
   * @return the current position
   */
  public Position getCurrent() {
    return this.current.get();
  }

  /**
   * Get the initial {@link Position}
   *
   * @return the initial position
   */
  public abstract Position initial();

  /**
   * Get the next {@link Position}
   *
   * @param current the current position
   *
   * @return the next position
   *
   * @throws NoSuchElementException if there is no next position
   */
  public abstract Position getContinue(Position current) throws NoSuchElementException;

  /**
   * Check if there is a next {@link Position}
   *
   * @param current the current position
   *
   * @return true if there is a next position
   */
  public abstract boolean hasContinue(Position current);

  @Override
  public void reset() {
    this.current.set(null);
  }

  @Override
  public boolean hasNext() {
    Position position = this.current.get();
    return position == null || hasContinue(position);
  }

  @Override
  public Position next() {
    Position position = getCurrent();
    if (position == null) {
      position = initial();
    } else {
      position = getContinue(position);
    }
    this.current.set(position);
    return position;
  }
}
