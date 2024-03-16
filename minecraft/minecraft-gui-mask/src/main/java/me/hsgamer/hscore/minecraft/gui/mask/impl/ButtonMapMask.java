package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The mask with a map of slot and button
 */
public class ButtonMapMask extends BaseMask {
  private final List<Button> buttons = new ArrayList<>();
  private final Map<Integer, Button> buttonMap = new HashMap<>();

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  public ButtonMapMask(@NotNull String name) {
    super(name);
  }

  /**
   * Add a button to the mask
   *
   * @param button the button
   * @param slots  the slots
   *
   * @return this instance
   */
  @Contract("_, _ -> this")
  public ButtonMapMask addButton(@NotNull Button button, @NotNull List<@NotNull Integer> slots) {
    buttons.add(button);
    slots.forEach(slot -> buttonMap.put(slot, button));
    return this;
  }

  /**
   * Add a button to the mask
   *
   * @param button the button
   * @param slots  the slots
   *
   * @return this instance
   */
  @Contract("_, _ -> this")
  public ButtonMapMask addButton(@NotNull Button button, int... slots) {
    buttons.add(button);
    for (int slot : slots) {
      buttonMap.put(slot, button);
    }
    return this;
  }

  /**
   * Get the buttons
   *
   * @return the buttons
   */
  @NotNull
  public List<Button> getButtons() {
    return Collections.unmodifiableList(buttons);
  }

  /**
   * Get the button map
   *
   * @return the button map
   */
  @NotNull
  public Map<Integer, Button> getButtonMap() {
    return Collections.unmodifiableMap(buttonMap);
  }

  @Override
  public Optional<Map<Integer, Button>> generateButtons(@NotNull UUID uuid, @NotNull InventorySize inventorySize) {
    return Optional.of(buttonMap);
  }

  @Override
  public void init() {
    buttons.forEach(Button::init);
  }

  @Override
  public void stop() {
    buttons.forEach(Button::stop);
  }
}
