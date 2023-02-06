package me.hsgamer.hscore.bukkit.block;

import me.hsgamer.hscore.minecraft.block.box.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

/**
 * The block adapter for Bukkit
 */
public class BukkitBlockAdapter {
  private BukkitBlockAdapter() {
    // EMPTY
  }

  /**
   * Adapt the vector to position
   *
   * @param vector    the vector
   * @param normalize whether to normalize the position. If true, the position will be rounded to the nearest integer
   *
   * @return the position
   */
  public static Position adapt(Vector vector, boolean normalize) {
    return normalize
      ? new Position(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ())
      : new Position(vector.getX(), vector.getY(), vector.getZ());
  }

  /**
   * Adapt the vector to position. The position will be rounded to the nearest integer.
   *
   * @param vector the vector
   *
   * @return the position
   */
  public static Position adapt(Vector vector) {
    return adapt(vector, true);
  }

  /**
   * Adapt the location to position
   *
   * @param location  the location
   * @param normalize whether to normalize the position. If true, the position will be rounded to the nearest integer
   *
   * @return the position
   */
  public static Position adapt(Location location, boolean normalize) {
    return adapt(location.toVector(), normalize);
  }

  /**
   * Adapt the location to position. The position will be rounded to the nearest integer.
   *
   * @param location the location
   *
   * @return the position
   */
  public static Position adapt(Location location) {
    return adapt(location, true);
  }

  /**
   * Adapt the block to position
   *
   * @param block     the block
   * @param normalize whether to normalize the position. If true, the position will be rounded to the nearest integer
   *
   * @return the position
   */
  public static Position adapt(Block block, boolean normalize) {
    return adapt(block.getLocation(), normalize);
  }

  /**
   * Adapt the block to position. The position will be rounded to the nearest integer.
   *
   * @param block the block
   *
   * @return the position
   */
  public static Position adapt(Block block) {
    return adapt(block, true);
  }

  /**
   * Adapt the position to vector
   *
   * @param position the position
   *
   * @return the vector
   */
  public static Vector adapt(Position position) {
    return new Vector(position.x, position.y, position.z);
  }

  /**
   * Adapt the position to location
   *
   * @param world    the world
   * @param position the position
   *
   * @return the location
   */
  public static Location adapt(World world, Position position) {
    return adapt(position).toLocation(world);
  }

  /**
   * Adapt the position to block
   *
   * @param world    the world
   * @param position the position
   *
   * @return the block
   */
  public static Block adaptAsBlock(World world, Position position) {
    return adapt(world, position).getBlock();
  }
}
