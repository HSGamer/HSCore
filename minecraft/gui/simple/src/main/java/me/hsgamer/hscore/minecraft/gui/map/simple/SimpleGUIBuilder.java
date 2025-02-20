package me.hsgamer.hscore.minecraft.gui.map.simple;

import me.hsgamer.hscore.minecraft.gui.common.button.Button;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The builder for {@link SimpleButtonMap}
 */
public class SimpleGUIBuilder {

  /**
   * the button map
   */
  @NotNull
  private final SimpleButtonMap simpleButtonMap;

  /**
   * current slot
   */
  private final AtomicInteger slot = new AtomicInteger();

  /**
   * ctor
   *
   * @param simpleButtonMap the button map
   */
  private SimpleGUIBuilder(@NotNull final SimpleButtonMap simpleButtonMap) {
    this.simpleButtonMap = simpleButtonMap;
  }

  /**
   * Create a new instance of {@code this}
   *
   * @param simpleButtonMap the button map
   *
   * @return a new builder
   */
  @NotNull
  public static SimpleGUIBuilder create(@NotNull final SimpleButtonMap simpleButtonMap) {
    return new SimpleGUIBuilder(simpleButtonMap);
  }

  /**
   * Create a new instance of {@code this} with an empty {@link SimpleButtonMap}
   *
   * @return a new builder
   */
  @NotNull
  public static SimpleGUIBuilder create() {
    return create(new SimpleButtonMap());
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
    this.simpleButtonMap.setButton(this.slot.get(), button);
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

  /**
   * Build the {@link SimpleButtonMap}
   *
   * @return the {@link SimpleButtonMap}
   */
  public SimpleButtonMap get() {
    return simpleButtonMap;
  }
}
