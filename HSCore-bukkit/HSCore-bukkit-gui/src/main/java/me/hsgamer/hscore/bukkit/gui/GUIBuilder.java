package me.hsgamer.hscore.bukkit.gui;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The builder for {@link GUIHolder}
 */
public class GUIBuilder {

  /**
   * the holder
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
  private GUIBuilder(@NotNull final GUIHolder holder) {
    this.holder = holder;
  }

  /**
   * Create a new instance of {@code this}
   *
   * @param holder the GUI holder
   *
   * @return a new builder
   */
  @NotNull
  public static GUIBuilder create(@NotNull final GUIHolder holder) {
    return new GUIBuilder(holder);
  }

  /**
   * Add the given button to the holder and increase {@link #slot} by 1
   *
   * @param button the button to add
   *
   * @return {@code this} for builder chain
   */
  @NotNull
  public GUIBuilder add(@NotNull final Button button) {
    this.holder.setButton(this.slot.get(), button);
    return this.next();
  }

  /**
   * Increase {@link #slot} by 1
   *
   * @return {@code this} for builder chain
   */
  @NotNull
  public GUIBuilder next() {
    return this.next(1);
  }

  /**
   * Increase {@link #slot} by the given slot size
   *
   * @param slot the slot to increase
   *
   * @return {@code this} for builder chain
   */
  @NotNull
  public GUIBuilder next(final int slot) {
    this.slot.addAndGet(slot);
    return this;
  }
}
