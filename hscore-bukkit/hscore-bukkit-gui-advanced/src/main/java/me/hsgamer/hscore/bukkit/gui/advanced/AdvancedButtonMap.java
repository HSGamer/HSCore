package me.hsgamer.hscore.bukkit.gui.advanced;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.bukkit.gui.button.ButtonMap;
import me.hsgamer.hscore.bukkit.gui.mask.Mask;

import java.util.*;
import java.util.stream.Collectors;

public class AdvancedButtonMap implements ButtonMap {
  private final List<Mask> masks = new LinkedList<>();

  /**
   * Add a mask
   *
   * @param mask the mask
   */
  public void addMask(Mask mask) {
    masks.add(mask);
  }

  /**
   * Remove masks by name
   *
   * @param name the name of the mask
   */
  public void removeMask(String name) {
    masks.removeIf(mask -> mask.getName().equals(name));
  }

  /**
   * Remove all masks
   *
   * @return the removed masks
   */
  public Collection<Mask> removeAllMasks() {
    List<Mask> removedMasks = new LinkedList<>(this.masks);
    masks.clear();
    return removedMasks;
  }

  /**
   * Get masks by name
   *
   * @param name the name of the mask
   *
   * @return the list of masks
   */
  public List<Mask> getMasks(String name) {
    return masks.parallelStream().filter(mask -> mask.getName().equals(name)).collect(Collectors.toList());
  }

  /**
   * Get all masks
   *
   * @return the list of all masks
   */
  public List<Mask> getMasks() {
    return Collections.unmodifiableList(masks);
  }

  @Override
  public Map<Button, List<Integer>> getButtons(UUID uuid) {
    Map<Button, List<Integer>> buttonSlotMap = new LinkedHashMap<>();
    for (Mask mask : masks) {
      Map<Integer, Button> buttons = mask.generateButtons(uuid);
      buttons.forEach((slot, button) -> buttonSlotMap.computeIfAbsent(button, k -> new LinkedList<>()).add(slot));
    }
    return buttonSlotMap;
  }

  @Override
  public void stop() {
    removeAllMasks().forEach(Mask::stop);
  }
}
