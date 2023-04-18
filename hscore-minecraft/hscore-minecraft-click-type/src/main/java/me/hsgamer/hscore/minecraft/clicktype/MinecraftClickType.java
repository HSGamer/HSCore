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
   * Get the hotbar slot (0-8) of the click type if it is number click
   *
   * @return the number, -1 if it is not number click or undefined
   */
  int getHotbarSlot();
}
