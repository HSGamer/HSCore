package me.hsgamer.hscore.bukkit.gui.simple;

import me.hsgamer.hscore.bukkit.gui.GUIHolder;
import me.hsgamer.hscore.bukkit.gui.button.Button;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The simple UI Holder for Bukkit
 */
public class SimpleGUIHolder extends GUIHolder<SimpleGUIDisplay> {

  private final Map<Button, List<Integer>> buttonSlotMap = new ConcurrentHashMap<>();
  private Button defaultButton = Button.EMPTY;

  public SimpleGUIHolder(Plugin plugin, boolean removeDisplayOnClose) {
    super(plugin, removeDisplayOnClose);
  }

  public SimpleGUIHolder(Plugin plugin) {
    super(plugin);
  }

  /**
   * Set the button
   *
   * @param slot   the slot
   * @param button the button
   */
  public void setButton(int slot, Button button) {
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
  public Collection<Button> removeAllButton() {
    List<Button> buttons = new LinkedList<>(buttonSlotMap.keySet());
    buttonSlotMap.values().forEach(List::clear);
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
  public List<Button> getButtons(int slot) {
    return buttonSlotMap.entrySet().stream().parallel().filter(entry -> entry.getValue().contains(slot)).map(Map.Entry::getKey).collect(Collectors.toList());
  }

  /**
   * Get the map of buttons
   *
   * @return the map of buttons
   */
  public Map<Button, List<Integer>> getButtonSlotMap() {
    return Collections.unmodifiableMap(buttonSlotMap);
  }

  /**
   * Get the default button
   *
   * @return the button
   */
  public Button getDefaultButton() {
    return defaultButton;
  }

  /**
   * Set the default button
   *
   * @param defaultButton the button
   */
  public void setDefaultButton(Button defaultButton) {
    this.defaultButton = defaultButton;
  }

  @Override
  protected SimpleGUIDisplay newDisplay(UUID uuid) {
    return new SimpleGUIDisplay(uuid, this);
  }

  @Override
  public void stop() {
    removeAllButton().forEach(Button::stop);
    super.stop();
  }
}
