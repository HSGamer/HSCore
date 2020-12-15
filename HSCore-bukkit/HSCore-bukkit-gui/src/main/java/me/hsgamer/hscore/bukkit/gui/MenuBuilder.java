package me.hsgamer.hscore.bukkit.gui;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The menu builder for {@link GUIHolder}
 */
public class MenuBuilder {

  /**
   * menu itself
   */
  @NotNull
  private final GUIHolder holder;

  /**
   * current slot
   */
  private final AtomicInteger slot = new AtomicInteger();

  /**
   * ctor
   *
   * @param holder the holder
   */
  private MenuBuilder(@NotNull final GUIHolder holder) {
    this.holder = holder;
  }

  /**
   * Create a new instance of {@code this}
   *
   * @return a new builder
   */
  @NotNull
  public static MenuBuilder create(@NotNull final GUIHolder holder) {
    return new MenuBuilder(holder);
  }

  /**
   * Add the given button to the holder and increase {@link #slot} by 1
   *
   * @param button the button to add
   *
   * @return {@code this} for builder chain
   */
  @NotNull
  public MenuBuilder add(@NotNull final Button button) {
    this.holder.setButton(this.slot.get(), button);
    return this.next();
  }

  /**
   * Increase {@link #slot} by 1
   *
   * @return {@code this} for builder chain
   */
  @NotNull
  public MenuBuilder next() {
    return this.next(1);
  }

  /**
   * Increase {@link #slot} by the given slot size
   *
   * @return {@code this} for builder chain
   */
  @NotNull
  public MenuBuilder next(final int slot) {
    this.slot.addAndGet(slot);
    return this;
  }
}
