package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The {@link SequencePaginatedMask} with the static list of buttons
 */
public class StaticSequencePaginatedMask extends SequencePaginatedMask {
  protected final List<Button> buttons = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param name  the name of the mask
   * @param slots the slots
   */
  public StaticSequencePaginatedMask(@NotNull String name, @NotNull List<@NotNull Integer> slots) {
    super(name, slots);
  }

  /**
   * Add button(s)
   *
   * @param buttons the buttons
   * @param <T>     the type of the button
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public <T extends Button> StaticSequencePaginatedMask addButton(@NotNull Collection<@NotNull T> buttons) {
    this.buttons.addAll(buttons);
    return this;
  }

  /**
   * Add button(s)
   *
   * @param button the button
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public StaticSequencePaginatedMask addButton(@NotNull Button... button) {
    return addButton(Arrays.asList(button));
  }

  @Override
  public @NotNull List<Button> getButtons(UUID uuid) {
    return this.buttons;
  }

  @Override
  public void init() {
    this.buttons.forEach(Button::init);
  }

  @Override
  public void stop() {
    this.buttons.forEach(Button::stop);
    this.buttons.clear();
    super.stop();
  }
}
