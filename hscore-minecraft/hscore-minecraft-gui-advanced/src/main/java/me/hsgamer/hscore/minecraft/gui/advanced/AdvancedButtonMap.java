package me.hsgamer.hscore.minecraft.gui.advanced;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An advanced {@link ButtonMap} that uses {@link Mask}
 */
public class AdvancedButtonMap implements ButtonMap {
  private final List<Mask> masks = new LinkedList<>();
  private boolean allowSlotDuplication = false;

  /**
   * Add a mask
   *
   * @param mask the mask
   */
  public void addMask(@NotNull Mask mask) {
    masks.add(mask);
  }

  /**
   * Remove masks by name
   *
   * @param name the name of the mask
   */
  public void removeMask(@NotNull String name) {
    masks.removeIf(mask -> mask.getName().equals(name));
  }

  /**
   * Remove all masks
   *
   * @return the removed masks
   */
  @NotNull
  public Collection<@NotNull Mask> removeAllMasks() {
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
  @NotNull
  public List<@NotNull Mask> getMasks(@NotNull String name) {
    return masks.parallelStream().filter(mask -> mask.getName().equals(name)).collect(Collectors.toList());
  }

  /**
   * Get all masks
   *
   * @return the list of all masks
   */
  @NotNull
  public List<@NotNull Mask> getMasks() {
    return Collections.unmodifiableList(masks);
  }

  /**
   * Get whether to allow slot duplication
   *
   * @return whether to allow slot duplication
   */
  public boolean isAllowSlotDuplication() {
    return allowSlotDuplication;
  }

  /**
   * Set whether to allow slot duplication when generating buttons from masks.
   * If it's true, the button will be added to the slot even if there is already a button in the slot.
   * If it's false, the button will override the button in the slot.
   *
   * @param allowSlotDuplication whether to allow slot duplication
   */
  public void setAllowSlotDuplication(boolean allowSlotDuplication) {
    this.allowSlotDuplication = allowSlotDuplication;
  }

  @Override
  public @NotNull Map<Button, Collection<Integer>> getButtons(@NotNull UUID uuid) {
    Map<Button, Collection<Integer>> buttonSlotMap = new LinkedHashMap<>();
    if (allowSlotDuplication) {
      for (Mask mask : masks) {
        if (!mask.canView(uuid)) continue;
        Map<Integer, Button> buttons = mask.generateButtons(uuid);
        buttons.forEach((slot, button) -> buttonSlotMap.computeIfAbsent(button, k -> new ArrayList<>()).add(slot));
      }
    } else {
      Map<Integer, Button> slotMap = new LinkedHashMap<>();
      for (Mask mask : masks) {
        if (!mask.canView(uuid)) continue;
        Map<Integer, Button> buttons = mask.generateButtons(uuid);
        slotMap.putAll(buttons);
      }
      slotMap.forEach((slot, button) -> buttonSlotMap.computeIfAbsent(button, k -> new ArrayList<>()).add(slot));
    }
    return buttonSlotMap;
  }

  @Override
  public void stop() {
    removeAllMasks().forEach(Mask::stop);
  }
}
