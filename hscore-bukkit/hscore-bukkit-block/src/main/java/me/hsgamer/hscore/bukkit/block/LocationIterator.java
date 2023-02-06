package me.hsgamer.hscore.bukkit.block;

import me.hsgamer.hscore.minecraft.block.adapt.WrappedPositionIterator;
import me.hsgamer.hscore.minecraft.block.box.Position;
import me.hsgamer.hscore.minecraft.block.iterator.PositionIterator;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * The {@link PositionIterator} for {@link Location}
 */
public class LocationIterator extends WrappedPositionIterator<Location> {
  private final World world;

  /**
   * Create a new iterator
   *
   * @param world            the world
   * @param positionIterator the position iterator
   */
  protected LocationIterator(World world, PositionIterator positionIterator) {
    super(positionIterator);
    this.world = world;
  }

  @Override
  protected Location convert(Position position) {
    return new Location(world, position.x, position.y, position.z);
  }
}
