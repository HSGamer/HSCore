package me.hsgamer.hscore.minecraft.gui;

/**
 * The extra properties for the GUI
 */
public class GUIProperties {
  private static long millisPerTick = 50L;

  private GUIProperties() {
    // EMPTY
  }

  /**
   * Get the millis per tick
   *
   * @return the millis per tick
   */
  public static long getMillisPerTick() {
    return millisPerTick;
  }

  /**
   * Set the millis per tick
   *
   * @param millisPerTick the millis per tick
   */
  public static void setMillisPerTick(long millisPerTick) {
    GUIProperties.millisPerTick = Math.max(millisPerTick, 1);
  }
}
