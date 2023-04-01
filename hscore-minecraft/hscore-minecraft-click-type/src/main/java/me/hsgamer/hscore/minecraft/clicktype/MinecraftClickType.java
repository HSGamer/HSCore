package me.hsgamer.hscore.minecraft.clicktype;

/**
 * The click type of Minecraft
 */
public interface MinecraftClickType {
  /**
   * Get the name of the click type
   *
   * @return the name
   */
  String getName();

  /**
   * Check if the click type is left click
   *
   * @return true if it is
   */
  boolean isLeftClick();

  /**
   * Check if the click type is right click
   *
   * @return true if it is
   */
  boolean isRightClick();

  /**
   * Check if the click type is shift click
   *
   * @return true if it is
   */
  boolean isShiftClick();

  /**
   * Check if the click type is number click
   *
   * @return true if it is
   */
  boolean isNumberClick();

  /**
   * Get the number of the click type
   *
   * @return the number
   */
  int getNumber();
}
