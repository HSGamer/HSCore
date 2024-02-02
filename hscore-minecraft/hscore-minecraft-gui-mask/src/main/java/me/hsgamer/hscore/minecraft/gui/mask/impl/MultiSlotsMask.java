package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import me.hsgamer.hscore.minecraft.gui.mask.MaskSlot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The masks with multiple slot
 */
public class MultiSlotsMask extends BaseMask {
  protected final MaskSlot maskSlot;
  protected final List<Button> buttons = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param name     the name of the mask
   * @param maskSlot the mask slot
   */
  public MultiSlotsMask(@NotNull String name, @NotNull MaskSlot maskSlot) {
    super(name);
    this.maskSlot = maskSlot;
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
   * Get the mask slot
   *
   * @return the mask slot
   */
  @NotNull
  public MaskSlot getMaskSlot() {
    return maskSlot;
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
    List<Integer> slots = this.maskSlot.getSlots();
    if (!this.buttons.isEmpty() && !slots.isEmpty()) {
      int slotsSize = slots.size();
      int buttonsSize = this.buttons.size();
      for (int i = 0; i < slotsSize; i++) {
        map.put(slots.get(i), this.buttons.get(i % buttonsSize));
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
