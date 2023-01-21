package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
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
   * @param name    the name of the mask
   * @param slots   the slots
   * @param buttons the buttons
   */
  public MultiSlotsMask(@NotNull String name, @NotNull List<@NotNull Integer> slots, @NotNull Button... buttons) {
    super(name);
    this.slots.addAll(slots);
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
  public @NotNull Map<Integer, Button> generateButtons(@NotNull UUID uuid) {
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
