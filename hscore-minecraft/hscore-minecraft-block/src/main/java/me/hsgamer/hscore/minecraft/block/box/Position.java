package me.hsgamer.hscore.minecraft.block.box;

import java.util.Objects;

/**
 * The immutable 3D position / location / vector
 */
public class Position {
  /**
   * The x-axis
   */
  public final double x;
  /**
   * The y-axis
   */
  public final double y;
  /**
   * The z-axis
   */
  public final double z;

  /**
   * Create a new {@link Position}
   *
   * @param x the x-axis
   * @param y the y-axis
   * @param z the z-axis
   */
  public Position(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Create a new {@link Position} that offset from this position
   *
   * @param x the x-axis offset
   * @param y the y-axis offset
   * @param z the z-axis offset
   *
   * @return the new position
   */
  public Position move(double x, double y, double z) {
    return new Position(this.x + x, this.y + y, this.z + z);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Position position = (Position) o;
    return Double.compare(position.x, x) == 0 && Double.compare(position.y, y) == 0 && Double.compare(position.z, z) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }
}
