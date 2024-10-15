package me.hsgamer.hscore.minecraft.gui.mask;

import org.jetbrains.annotations.NotNull;

/**
 * The base class for {@link Mask}
 */
public abstract class BaseMask implements Mask {
  private final String name;

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  protected BaseMask(@NotNull String name) {
    this.name = name;
  }

  @Override
  public @NotNull String getName() {
    return this.name;
  }
}
