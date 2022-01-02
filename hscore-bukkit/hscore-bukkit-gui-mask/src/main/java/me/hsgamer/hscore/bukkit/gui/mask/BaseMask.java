package me.hsgamer.hscore.bukkit.gui.mask;

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
  protected BaseMask(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return this.name;
  }
}
