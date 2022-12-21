package me.hsgamer.hscore.bukkit.block.iterator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Iterator;

/**
 * The {@link Iterator} for {@link Vector}
 */
public interface VectorIterator extends Iterator<Vector> {
  /**
   * Reset the iterator
   */
  void reset();

  /**
   * Get the next {@link Location}
   *
   * @param world the world
   *
   * @return the next location
   */
  default Location nextLocation(World world) {
    return this.next().toLocation(world);
  }
}
