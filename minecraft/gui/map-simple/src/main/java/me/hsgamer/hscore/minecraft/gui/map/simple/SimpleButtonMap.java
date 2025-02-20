package me.hsgamer.hscore.minecraft.gui.map.simple;

import me.hsgamer.hscore.minecraft.gui.common.button.Button;
import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryPosition;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * A simple button map with a list of {@link Button}s
 */
public class SimpleButtonMap implements ButtonMap {
  private final Map<Button, Collection<Function<InventoryContext, Integer>>> buttonSlotMap = new LinkedHashMap<>();
  private Button defaultButton = Button.EMPTY;

  /**
   * Set the button
   *
   * @param slot   the slot
   * @param button the button
   */
  public void setButton(int slot, @NotNull Button button) {
    buttonSlotMap.computeIfAbsent(button, b -> new LinkedList<>()).add(context -> slot);
  }

  /**
   * Set the button
   *
   * @param position the position
   * @param button   the button
   */
  public void setButton(InventoryPosition position, @NotNull Button button) {
    buttonSlotMap.computeIfAbsent(button, b -> new LinkedList<>()).add(context -> context.getSlot(position));
  }

  /**
   * Get the default button
   *
   * @return the button
   */
  public @NotNull Button getDefaultButton() {
    return defaultButton;
  }

  /**
   * Set the default button
   *
   * @param defaultButton the button
   */
  public void setDefaultButton(@NotNull Button defaultButton) {
    this.defaultButton = defaultButton;
  }

  @Override
  public void init() {
    buttonSlotMap.keySet().forEach(Button::init);
    defaultButton.init();
  }

  @Override
  public void stop() {
    buttonSlotMap.keySet().forEach(Button::stop);
    buttonSlotMap.clear();
    defaultButton.stop();
  }

  @Override
  public @NotNull Map<Integer, ActionItem> getItemMap(InventoryContext context) {
    Map<Integer, ActionItem> map = new HashMap<>();
    IntFunction<ActionItem> getDisplayButton = i -> map.computeIfAbsent(i, s -> new ActionItem());

    ActionItem defaultActionItem = defaultButton.getItem(context);
    if (defaultActionItem != null) {
      for (int i = 0; i < context.getInventorySize(); i++) {
        getDisplayButton.apply(i).apply(defaultActionItem);
      }
    }

    buttonSlotMap.forEach((button, slots) -> {
      ActionItem actionItem = button.getItem(context);
      if (actionItem == null) return;
      slots.forEach(slot -> getDisplayButton.apply(slot.apply(context)).apply(actionItem));
    });

    return map;
  }
}
