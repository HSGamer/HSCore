package me.hsgamer.hscore.minecraft.gui.object;

/**
 * The position of the inventory
 */
public class InventoryPosition {
  private final int x;
  private final int y;

  /**
   * Creates a new position
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  private InventoryPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Creates a new position
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public static InventoryPosition of(int x, int y) {
    return new InventoryPosition(x, y);
  }

  /**
   * Get the max position
   *
   * @param x1 the x of the first position
   * @param y1 the y of the first position
   * @param x2 the x of the second position
   * @param y2 the y of the second position
   *
   * @return the max position
   */
  public static InventoryPosition maxPosition(int x1, int y1, int x2, int y2) {
    return InventoryPosition.of(Math.max(x1, x2), Math.max(y1, y2));
  }

  /**
   * Get the max position
   *
   * @param position1 the first position
   * @param position2 the second position
   *
   * @return the max position
   */
  public static InventoryPosition maxPosition(InventoryPosition position1, InventoryPosition position2) {
    return maxPosition(position1.getX(), position1.getY(), position2.getX(), position2.getY());
  }

  /**
   * Get the min position
   *
   * @param x1 the x of the first position
   * @param y1 the y of the first position
   * @param x2 the x of the second position
   * @param y2 the y of the second position
   *
   * @return the min position
   */
  public static InventoryPosition minPosition(int x1, int y1, int x2, int y2) {
    return InventoryPosition.of(Math.min(x1, x2), Math.min(y1, y2));
  }

  /**
   * Get the min position
   *
   * @param position1 the first position
   * @param position2 the second position
   *
   * @return the min position
   */
  public static InventoryPosition minPosition(InventoryPosition position1, InventoryPosition position2) {
    return minPosition(position1.getX(), position1.getY(), position2.getX(), position2.getY());
  }

  /**
   * Get the x coordinate
   *
   * @return the x coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Get the y coordinate
   *
   * @return the y coordinate
   */
  public int getY() {
    return y;
  }
}
