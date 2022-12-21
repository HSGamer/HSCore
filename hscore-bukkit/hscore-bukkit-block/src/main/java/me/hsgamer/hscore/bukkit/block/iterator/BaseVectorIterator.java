package me.hsgamer.hscore.bukkit.block.iterator;

import me.hsgamer.hscore.bukkit.block.box.BlockBox;
import org.bukkit.util.Vector;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The base {@link VectorIterator} for {@link BlockBox}
 */
public abstract class BaseVectorIterator extends AbstractVectorIterator {
  private final AtomicReference<Vector> current;

  /**
   * Create a new {@link BaseVectorIterator}
   *
   * @param box the box
   */
  protected BaseVectorIterator(BlockBox box) {
    super(box);
    this.current = new AtomicReference<>();
  }

  /**
   * Get the current {@link Vector}
   *
   * @return the current vector
   */
  public Vector getCurrent() {
    return this.current.get();
  }

  /**
   * Get the initial {@link Vector}
   *
   * @return the initial vector
   */
  public abstract Vector initial();

  /**
   * Get the next {@link Vector}
   *
   * @param current the current vector
   *
   * @return the next vector
   *
   * @throws NoSuchElementException if there is no next vector
   */
  public abstract Vector getContinue(Vector current) throws NoSuchElementException;

  /**
   * Check if there is a next {@link Vector}
   *
   * @param current the current vector
   *
   * @return true if there is a next vector
   */
  public abstract boolean hasContinue(Vector current);

  @Override
  public void reset() {
    this.current.set(null);
  }

  @Override
  public boolean hasNext() {
    Vector vector = this.current.get();
    return vector == null || hasContinue(vector);
  }

  @Override
  public Vector next() {
    Vector vector = getCurrent();
    if (vector == null) {
      vector = initial();
    } else {
      vector = getContinue(vector);
    }
    this.current.set(vector);
    return vector;
  }
}
