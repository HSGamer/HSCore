package me.hsgamer.hscore.minecraft.gui.advanced;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.button.DisplayButton;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * An advanced {@link ButtonMap} that uses {@link Mask}
 */
public class AdvancedButtonMap implements ButtonMap {
  private final List<Mask> masks = new LinkedList<>();

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

  @Override
  public void stop() {
    removeAllMasks().forEach(Mask::stop);
  }

  @Override
  public @NotNull Map<@NotNull Integer, @NotNull DisplayButton> getButtons(@NotNull UUID uuid, int size) {
    Map<Integer, DisplayButton> map = new HashMap<>();
    for (Mask mask : masks) {
      Optional<Map<Integer, Button>> buttons = mask.generateButtons(uuid, size);
      if (!buttons.isPresent()) continue;
      buttons.get().forEach((slot, button) -> {
        if (slot < 0 || slot >= size) {
          return;
        }

        DisplayButton displayButton = button.display(uuid);
        if (displayButton == null) {
          return;
        }

        DisplayButton currentDisplayButton = map.computeIfAbsent(slot, s -> new DisplayButton());
        Item item = displayButton.getItem();
        if (item != null) {
          currentDisplayButton.setItem(item);
        }
        Consumer<ClickEvent> action = displayButton.getClickAction();
        if (action != null) {
          currentDisplayButton.setClickAction(action);
        }
      });
    }
    return map;
  }
}
