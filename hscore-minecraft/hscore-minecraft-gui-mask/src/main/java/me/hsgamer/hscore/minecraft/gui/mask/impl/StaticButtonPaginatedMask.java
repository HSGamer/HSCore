package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.MaskSlot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The {@link ButtonPaginatedMask} with the static list of buttons
 */
public class StaticButtonPaginatedMask extends ButtonPaginatedMask {
  protected final List<Button> buttons = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param name     the name of the mask
   * @param maskSlot the mask slot
   */
  public StaticButtonPaginatedMask(@NotNull String name, @NotNull MaskSlot maskSlot) {
    super(name, maskSlot);
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
  public <T extends Button> StaticButtonPaginatedMask addButton(@NotNull Collection<@NotNull T> buttons) {
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
  public StaticButtonPaginatedMask addButton(@NotNull Button... button) {
    return addButton(Arrays.asList(button));
  }

  @Override
  public @NotNull List<Button> getButtons(@NotNull UUID uuid) {
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
