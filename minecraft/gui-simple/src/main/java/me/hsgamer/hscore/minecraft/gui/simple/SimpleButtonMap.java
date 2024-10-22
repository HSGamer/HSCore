package me.hsgamer.hscore.minecraft.gui.simple;

import me.hsgamer.hscore.minecraft.gui.GUI;
import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.object.ActionItem;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import me.hsgamer.hscore.ui.property.Initializable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/**
 * A simple button map with a list of {@link Button}s
 */
public class SimpleButtonMap implements Initializable {
  private final Map<Button, Collection<Integer>> buttonSlotMap = new LinkedHashMap<>();
  private Button defaultButton = Button.EMPTY;

  /**
   * Set the button
   *
   * @param slot   the slot
   * @param button the button
   */
  public void setButton(int slot, @NotNull Button button) {
    buttonSlotMap.computeIfAbsent(button, b -> new LinkedList<>()).add(slot);
  }

  /**
   * Remove the button
   *
   * @param slot the slot
   */
  public void removeButton(int slot) {
    buttonSlotMap.values().forEach(list -> list.removeIf(i -> i == slot));
  }

  /**
   * Remove all buttons
   *
   * @return all cleared buttons
   */
  @NotNull
  public Collection<Button> removeAllButton() {
    List<Button> buttons = new LinkedList<>(buttonSlotMap.keySet());
    buttonSlotMap.values().forEach(Collection::clear);
    buttonSlotMap.clear();
    return buttons;
  }

  /**
   * Get buttons by the slot
   *
   * @param slot the slot
   *
   * @return the button
   */
  @NotNull
  public Collection<Button> getButtons(int slot) {
    return buttonSlotMap.entrySet().stream().parallel().filter(entry -> entry.getValue().contains(slot)).map(Map.Entry::getKey).collect(Collectors.toList());
  }

  /**
   * Get the map of buttons
   *
   * @return the map of buttons
   */
  public Map<Button, Collection<Integer>> getButtonSlotMap() {
    return Collections.unmodifiableMap(buttonSlotMap);
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
  public void stop() {
    removeAllButton().forEach(Button::stop);
  }

  /**
   * Get the buttons
   *
   * @param uuid          the unique id
   * @param inventorySize the inventory size
   *
   * @return the buttons
   */
  public @NotNull Map<@NotNull Integer, @NotNull ActionItem> getButtons(@NotNull UUID uuid, InventorySize inventorySize) {
    Map<Integer, ActionItem> map = new HashMap<>();
    IntFunction<ActionItem> getDisplayButton = i -> map.computeIfAbsent(i, s -> new ActionItem());

    Button defaultButton = getDefaultButton();
    ActionItem defaultActionItem = defaultButton.display(uuid);
    if (defaultActionItem != null) {
      inventorySize.getSlots().forEach(i -> getDisplayButton.apply(i).apply(defaultActionItem));
    }

    buttonSlotMap.forEach((button, slots) -> {
      ActionItem actionItem = button.display(uuid);
      if (actionItem == null) return;
      slots.forEach(slot -> getDisplayButton.apply(slot).apply(actionItem));
    });

    return map;
  }

  /**
   * Apply the buttons to the GUI
   *
   * @param uuid the unique id
   * @param gui  the GUI
   */
  public void apply(UUID uuid, GUI gui) {
    gui.updateItems(getButtons(uuid, gui.getInventorySize()));
  }
}
