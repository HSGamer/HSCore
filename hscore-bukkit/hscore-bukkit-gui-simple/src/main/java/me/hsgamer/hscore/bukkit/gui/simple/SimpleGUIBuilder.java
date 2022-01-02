package me.hsgamer.hscore.bukkit.gui.simple;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The builder for {@link SimpleGUIHolder}
 */
public class SimpleGUIBuilder {

  /**
   * the holder
   */
  @NotNull
  private final SimpleGUIHolder holder;

  /**
   * current slot
   */
  private final AtomicInteger slot = new AtomicInteger();

  /**
   * ctor
   *
   * @param holder the holder
   */
  private SimpleGUIBuilder(@NotNull final SimpleGUIHolder holder) {
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
  public static SimpleGUIBuilder create(@NotNull final SimpleGUIHolder holder) {
    return new SimpleGUIBuilder(holder);
  }

  /**
   * Add the given button to the holder and increase {@link #slot} by 1
   *
   * @param button the button to add
   *
   * @return {@code this} for builder chain
   */
  @NotNull
  public SimpleGUIBuilder add(@NotNull final Button button) {
    this.holder.setButton(this.slot.get(), button);
    return this.next();
  }

  /**
   * Increase {@link #slot} by 1
   *
   * @return {@code this} for builder chain
   */
  @NotNull
  public SimpleGUIBuilder next() {
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
  public SimpleGUIBuilder next(final int slot) {
    this.slot.addAndGet(slot);
    return this;
  }
}
