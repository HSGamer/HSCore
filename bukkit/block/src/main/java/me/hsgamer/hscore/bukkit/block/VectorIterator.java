package me.hsgamer.hscore.bukkit.block;

import me.hsgamer.hscore.minecraft.block.adapt.WrappedPositionIterator;
import me.hsgamer.hscore.minecraft.block.box.Position;
import me.hsgamer.hscore.minecraft.block.iterator.PositionIterator;
import org.bukkit.util.Vector;

/**
 * The {@link PositionIterator} for {@link Vector}
 */
public class VectorIterator extends WrappedPositionIterator<Vector> {
  /**
   * Create a new iterator
   *
   * @param positionIterator the position iterator
   */
  public VectorIterator(PositionIterator positionIterator) {
    super(positionIterator);
  }

  @Override
  protected Vector convert(Position position) {
    return new Vector(position.x, position.y, position.z);
  }
}
