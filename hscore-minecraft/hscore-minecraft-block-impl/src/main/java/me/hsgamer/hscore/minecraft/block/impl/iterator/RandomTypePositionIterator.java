package me.hsgamer.hscore.minecraft.block.impl.iterator;

import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.minecraft.block.box.BlockBox;
import me.hsgamer.hscore.minecraft.block.box.Position;
import me.hsgamer.hscore.minecraft.block.iterator.AbstractPositionIterator;
import me.hsgamer.hscore.minecraft.block.iterator.PositionIterator;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * The {@link PositionIterator} that combines multiple {@link PositionIterator} and randomly choose one
 */
public class RandomTypePositionIterator extends AbstractPositionIterator {
  private final Collection<Function<BlockBox, PositionIterator>> functions;
  private final AtomicReference<PositionIterator> current;

  /**
   * Create a new {@link RandomTypePositionIterator}
   *
   * @param box       the {@link BlockBox}
   * @param functions the functions to create {@link PositionIterator}
   */
  public RandomTypePositionIterator(BlockBox box, Collection<Function<BlockBox, PositionIterator>> functions) {
    super(box);
    this.functions = functions;
    current = new AtomicReference<>(getRandom());
  }

  private PositionIterator getRandom() {
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
  public Position next() {
    return current.get().next();
  }
}
