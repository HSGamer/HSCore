package me.hsgamer.hscore.minecraft.gui.map.simple;

import me.hsgamer.hscore.minecraft.gui.common.GUIElement;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryPosition;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * A simple button map
 */
public class SimpleButtonMap implements GUIElement, Function<@NotNull InventoryContext, @NotNull Map<Integer, ActionItem>> {
  private final Map<Function<@NotNull InventoryContext, @Nullable ActionItem>, Collection<Function<InventoryContext, Integer>>> buttonSlotMap = new LinkedHashMap<>();
  private Function<@NotNull InventoryContext, @Nullable ActionItem> defaultButton = context -> null;

  /**
   * Set the button
   *
   * @param slot   the slot
   * @param button the button
   */
  public void setButton(int slot, @NotNull Function<@NotNull InventoryContext, @Nullable ActionItem> button) {
    buttonSlotMap.computeIfAbsent(button, b -> new LinkedList<>()).add(context -> slot);
  }

  /**
   * Set the button
   *
   * @param x      the x coordinate
   * @param y      the y coordinate
   * @param button the button
   */
  public void setButton(int x, int y, @NotNull Function<@NotNull InventoryContext, @Nullable ActionItem> button) {
    buttonSlotMap.computeIfAbsent(button, b -> new LinkedList<>()).add(context -> context.getSlot(x, y));
  }

  /**
   * Set the button
   *
   * @param position the position
   * @param button   the button
   */
  public void setButton(InventoryPosition position, @NotNull Function<@NotNull InventoryContext, @Nullable ActionItem> button) {
    setButton(position.getX(), position.getY(), button);
  }

  /**
   * Get the default button
   *
   * @return the button
   */
  public @NotNull Function<@NotNull InventoryContext, @Nullable ActionItem> getDefaultButton() {
    return defaultButton;
  }

  /**
   * Set the default button
   *
   * @param defaultButton the button
   */
  public void setDefaultButton(@NotNull Function<@NotNull InventoryContext, @Nullable ActionItem> defaultButton) {
    this.defaultButton = defaultButton;
  }

  @Override
  public void init() {
    GUIElement.handleIfElement(buttonSlotMap.keySet(), GUIElement::init);
    GUIElement.handleIfElement(defaultButton, GUIElement::init);
  }

  @Override
  public void stop() {
    GUIElement.handleIfElement(buttonSlotMap.keySet(), GUIElement::stop);
    buttonSlotMap.clear();
    GUIElement.handleIfElement(defaultButton, GUIElement::stop);
  }

  @Override
  public @NotNull Map<Integer, ActionItem> apply(@NotNull InventoryContext context) {
    Map<Integer, ActionItem> map = new HashMap<>();
    IntFunction<ActionItem> getDisplayButton = i -> map.computeIfAbsent(i, s -> new ActionItem());

    ActionItem defaultActionItem = defaultButton.apply(context);
    if (defaultActionItem != null) {
      for (int i = 0; i < context.getSize(); i++) {
        getDisplayButton.apply(i).apply(defaultActionItem);
      }
    }

    buttonSlotMap.forEach((button, slots) -> {
      ActionItem actionItem = button.apply(context);
      if (actionItem == null) return;
      slots.forEach(slot -> getDisplayButton.apply(slot.apply(context)).apply(actionItem));
    });

    return map;
  }
}
