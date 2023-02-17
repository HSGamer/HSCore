package me.hsgamer.hscore.expansion.common;

public interface Expansion {
  /**
   * Called when loading the expansion
   *
   * @return whether the expansion loaded properly
   */
  default boolean onLoad() {
    return true;
  }

  /**
   * Called when enabling the expansion
   */
  default void onEnable() {
    // EMPTY
  }

  /**
   * Called when disabling the expansion
   */
  default void onDisable() {
    // EMPTY
  }
}
