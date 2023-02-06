package me.hsgamer.hscore.minecraft.block.box;

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
}
