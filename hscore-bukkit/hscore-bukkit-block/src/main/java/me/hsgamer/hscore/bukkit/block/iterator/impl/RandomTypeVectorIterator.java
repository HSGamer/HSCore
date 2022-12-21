package me.hsgamer.hscore.bukkit.block.iterator.impl;

import me.hsgamer.hscore.bukkit.block.box.BlockBox;
import me.hsgamer.hscore.bukkit.block.iterator.AbstractVectorIterator;
import me.hsgamer.hscore.bukkit.block.iterator.VectorIterator;
import me.hsgamer.hscore.common.CollectionUtils;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * The {@link VectorIterator} that combines multiple {@link VectorIterator} and randomly choose one
 */
public class RandomTypeVectorIterator extends AbstractVectorIterator {
  private final Collection<Function<BlockBox, VectorIterator>> functions;
  private final AtomicReference<VectorIterator> current;

  /**
   * Create a new {@link RandomVectorIterator}
   *
   * @param box       the {@link BlockBox}
   * @param functions the functions to create {@link VectorIterator}
   */
  public RandomTypeVectorIterator(BlockBox box, Collection<Function<BlockBox, VectorIterator>> functions) {
    super(box);
    this.functions = functions;
    current = new AtomicReference<>(getRandom());
  }

  private VectorIterator getRandom() {
    return Objects.requireNonNull(CollectionUtils.pickRandom(functions)).apply(box);
  }

  @Override
  public void reset() {
    current.set(getRandom());
  }

  @Override
  public boolean hasNext() {
    return current.get().hasNext();
  }

  @Override
  public Vector next() {
    return current.get().next();
  }
}
