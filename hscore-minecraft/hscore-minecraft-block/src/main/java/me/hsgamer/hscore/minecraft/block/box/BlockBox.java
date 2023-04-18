package me.hsgamer.hscore.minecraft.block.box;

import java.util.Objects;

/**
 * A custom box to bound blocks
 */
public class BlockBox {
  /**
   * The minimum x coordinate
   */
  public final int minX;
  /**
   * The minimum y coordinate
   */
  public final int minY;
  /**
   * The minimum z coordinate
   */
  public final int minZ;
  /**
   * The maximum x coordinate
   */
  public final int maxX;
  /**
   * The maximum y coordinate
   */
  public final int maxY;
  /**
   * The maximum z coordinate
   */
  public final int maxZ;

  /**
   * Create a new block box
   *
   * @param x1 the first x
   * @param y1 the first y
   * @param z1 the first z
   * @param x2 the second x
   * @param y2 the second y
   * @param z2 the second z
   */
  public BlockBox(int x1, int y1, int z1, int x2, int y2, int z2) {
    minX = Math.min(x1, x2);
    minY = Math.min(y1, y2);
    minZ = Math.min(z1, z2);
    maxX = Math.max(x1, x2);
    maxY = Math.max(y1, y2);
    maxZ = Math.max(z1, z2);
  }

  /**
   * Create a new block box
   *
   * @param pos1 the first position
   * @param pos2 the second position
   */
  public BlockBox(Position pos1, Position pos2) {
    this((int) pos1.x, (int) pos1.y, (int) pos1.z, (int) pos2.x, (int) pos2.y, (int) pos2.z);
  }

  /**
   * Create a new block box that increases the maximum position by the given offset
   *
   * @param x the x-axis offset
   * @param y the y-axis offset
   * @param z the z-axis offset
   *
   * @return the new box
   */
  public BlockBox expandMax(int x, int y, int z) {
    return new BlockBox(min(), max().move(x, y, z));
  }

  /**
   * Create a new block box that decreases the minimum position by the given offset
   *
   * @param x the x-axis offset
   * @param y the y-axis offset
   * @param z the z-axis offset
   *
   * @return the new box
   */
  public BlockBox expandMin(int x, int y, int z) {
    return new BlockBox(min().move(-x, -y, -z), max());
  }

  /**
   * Create a new block box that expands the minimum and maximum position by the given offset
   *
   * @param x the x-axis offset
   * @param y the y-axis offset
   * @param z the z-axis offset
   *
   * @return the new box
   */
  public BlockBox expand(int x, int y, int z) {
    return expandMin(x, y, z).expandMax(x, y, z);
  }

  /**
   * Create a new block box that increases the maximum position by one.
   * Use this to include the block at the maximum position as a part of the box.
   *
   * @return the new box
   */
  public BlockBox maxInclusive() {
    return expandMax(1, 1, 1);
  }

  /**
   * Check if the location is in the box
   *
   * @param x the x
   * @param y the y
   * @param z the z
   *
   * @return true if it is in the box
   */
  public boolean contains(double x, double y, double z) {
    return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
  }

  /**
   * Check if the location is in the box
   *
   * @param pos the position
   *
   * @return true if it is in the box
   */
  public boolean contains(Position pos) {
    return contains(pos.x, pos.y, pos.z);
  }

  /**
   * Get the center of the box
   *
   * @return the center
   */
  public Position center() {
    return new Position((minX + maxX) / 2.0, (minY + maxY) / 2.0, (minZ + maxZ) / 2.0);
  }

  /**
   * Get the minimum position
   *
   * @return the minimum position
   */
  public Position min() {
    return new Position(minX, minY, minZ);
  }

  /**
   * Get the maximum position
   *
   * @return the maximum position
   */
  public Position max() {
    return new Position(maxX, maxY, maxZ);
  }

  /**
   * Get the X-size of the box
   *
   * @return the size
   */
  public double sizeX() {
    return maxX - minX;
  }

  /**
   * Get the Y-size of the box
   *
   * @return the size
   */
  public double sizeY() {
    return maxY - minY;
  }

  /**
   * Get the Z-size of the box
   *
   * @return the size
   */
  public double sizeZ() {
    return maxZ - minZ;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BlockBox blockBox = (BlockBox) o;
    return minX == blockBox.minX && minY == blockBox.minY && minZ == blockBox.minZ && maxX == blockBox.maxX && maxY == blockBox.maxY && maxZ == blockBox.maxZ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(minX, minY, minZ, maxX, maxY, maxZ);
  }
}
