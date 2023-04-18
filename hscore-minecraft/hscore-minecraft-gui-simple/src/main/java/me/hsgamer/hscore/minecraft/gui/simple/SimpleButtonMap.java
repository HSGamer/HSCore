package me.hsgamer.hscore.minecraft.gui.simple;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.button.ViewedButton;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A simple {@link ButtonMap} with a list of {@link Button}s
 */
public class SimpleButtonMap implements ButtonMap {
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

  @Override
  public @NotNull Map<@NotNull Integer, @NotNull ViewedButton> getButtons(@NotNull UUID uuid, int size) {
    Map<Integer, ViewedButton> map = new HashMap<>();
    IntFunction<ViewedButton> getViewedButton = i -> map.computeIfAbsent(i, s -> new ViewedButton());

    List<Integer> emptyItemSlots = IntStream.range(0, size).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    List<Integer> emptyActionSlots = IntStream.range(0, size).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

    buttonSlotMap.forEach((button, slots) -> {
      Item item = button.getItem(uuid);
      if (item == null && !button.forceSetAction(uuid)) {
        return;
      }
      slots.forEach(slot -> {
        ViewedButton viewedButton = getViewedButton.apply(slot);
        if (item != null) {
          viewedButton.setDisplayItem(item);
          emptyItemSlots.remove(slot);
        }
        viewedButton.setButton(button);
        emptyActionSlots.remove(slot);
      });
    });

    Button defaultButton = getDefaultButton();
    Item defaultItem = defaultButton.getItem(uuid);
    if (defaultItem != null) {
      emptyItemSlots.forEach(slot -> getViewedButton.apply(slot).setDisplayItem(defaultItem));
    }
    emptyActionSlots.forEach(slot -> getViewedButton.apply(slot).setButton(defaultButton));

    return map;
  }
}
