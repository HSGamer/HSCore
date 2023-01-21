package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The {@link SequencePaginatedMask} with the static list of buttons
 */
public class StaticSequencePaginatedMask extends SequencePaginatedMask {
  protected final List<Button> buttons = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param name    the name of the mask
   * @param slots   the slots
   * @param buttons the buttons
   */
  public StaticSequencePaginatedMask(@NotNull String name, @NotNull List<@NotNull Integer> slots, @NotNull Button... buttons) {
    super(name, slots);
    this.addButtons(buttons);
  }

  /**
   * Create a new mask
   *
   * @param name    the name of the mask
   * @param slots   the slots
   * @param buttons the buttons
   */
  public StaticSequencePaginatedMask(@NotNull String name, @NotNull List<@NotNull Integer> slots, @NotNull List<@NotNull Button> buttons) {
    super(name, slots);
    this.addButtons(buttons);
  }

  /**
   * Add buttons to the mask
   *
   * @param buttons the buttons
   */
  public void addButtons(@NotNull Button... buttons) {
    this.addButtons(Arrays.asList(buttons));
  }

  /**
   * Add buttons to the mask
   *
   * @param buttons the buttons
   */
  public void addButtons(@NotNull List<@NotNull Button> buttons) {
    this.buttons.addAll(buttons);
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
