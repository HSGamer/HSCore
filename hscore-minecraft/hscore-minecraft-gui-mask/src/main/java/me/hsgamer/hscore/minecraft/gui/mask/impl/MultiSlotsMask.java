package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The masks with multiple slot
 */
public class MultiSlotsMask extends BaseMask {
  protected final List<Integer> slots = new ArrayList<>();
  protected final List<Button> buttons = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param name  the name of the mask
   * @param slots the slots
   */
  public MultiSlotsMask(@NotNull String name, @NotNull List<@NotNull Integer> slots) {
    super(name);
    this.slots.addAll(slots);
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
  public <T extends Button> MultiSlotsMask addButton(@NotNull Collection<@NotNull T> buttons) {
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
  public MultiSlotsMask addButton(@NotNull Button... button) {
    return addButton(Arrays.asList(button));
  }

  /**
   * Get the slots
   *
   * @return the slots
   */
  @NotNull
  public List<@NotNull Integer> getSlots() {
    return Collections.unmodifiableList(slots);
  }

  /**
   * Get the buttons
   *
   * @return the buttons
   */
  @NotNull
  public List<@NotNull Button> getButtons() {
    return Collections.unmodifiableList(buttons);
  }

  @Override
  public @NotNull Map<Integer, Button> generateButtons(@NotNull UUID uuid, int size) {
    Map<Integer, Button> map = new HashMap<>();
    if (!this.buttons.isEmpty()) {
      int slotsSize = this.slots.size();
      int buttonsSize = this.buttons.size();
      for (int i = 0; i < slotsSize; i++) {
        map.put(this.slots.get(i), this.buttons.get(i % buttonsSize));
      }
    }
    return map;
  }

  @Override
  public void init() {
    if (this.buttons.isEmpty()) {
      return;
    }
    this.buttons.forEach(Button::init);
  }

  @Override
  public void stop() {
    if (this.buttons.isEmpty()) {
      return;
    }
    this.buttons.forEach(Button::stop);
  }
}
