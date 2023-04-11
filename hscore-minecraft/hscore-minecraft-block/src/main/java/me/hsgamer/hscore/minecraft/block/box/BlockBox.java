package me.hsgamer.hscore.minecraft.block.box;

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
   * Whether it should include the maximum coordinate
   */
  public final boolean maxInclusive;

  /**
   * Create a new block box
   *
   * @param x1           the first x
   * @param y1           the first y
   * @param z1           the first z
   * @param x2           the second x
   * @param y2           the second y
   * @param z2           the second z
   * @param maxInclusive true if it should include the maximum location
   */
  public BlockBox(int x1, int y1, int z1, int x2, int y2, int z2, boolean maxInclusive) {
    minX = Math.min(x1, x2);
    minY = Math.min(y1, y2);
    minZ = Math.min(z1, z2);
    maxX = Math.max(x1, x2) + (maxInclusive ? 1 : 0);
    maxY = Math.max(y1, y2) + (maxInclusive ? 1 : 0);
    maxZ = Math.max(z1, z2) + (maxInclusive ? 1 : 0);
    this.maxInclusive = maxInclusive;
  }

  /**
   * Create a new block box
   *
   * @param pos1         the first position
   * @param pos2         the second position
   * @param maxInclusive true if it should include the maximum location
   */
  public BlockBox(Position pos1, Position pos2, boolean maxInclusive) {
    this((int) pos1.x, (int) pos1.y, (int) pos1.z, (int) pos2.x, (int) pos2.y, (int) pos2.z, maxInclusive);
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
    if (maxInclusive) {
      return new Position((minX + maxX) / 2.0, (minY + maxY) / 2.0, (minZ + maxZ) / 2.0);
    } else {
      return new Position((minX + maxX + 1) / 2.0, (minY + maxY + 1) / 2.0, (minZ + maxZ + 1) / 2.0);
    }
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
    if (maxInclusive) {
      return new Position(maxX - 1, maxY - 1, maxZ - 1);
    } else {
      return new Position(maxX, maxY, maxZ);
    }
  }

  /**
   * Get the X-size of the box
   *
   * @return the size
   */
  public double sizeX() {
    if (maxInclusive) {
      return maxX - minX;
    } else {
      return maxX - minX + 1;
    }
  }

  /**
   * Get the Y-size of the box
   *
   * @return the size
   */
  public double sizeY() {
    if (maxInclusive) {
      return maxY - minY;
    } else {
      return maxY - minY + 1;
    }
  }

  /**
   * Get the Z-size of the box
   *
   * @return the size
   */
  public double sizeZ() {
    if (maxInclusive) {
      return maxZ - minZ;
    } else {
      return maxZ - minZ + 1;
    }
  }
}
