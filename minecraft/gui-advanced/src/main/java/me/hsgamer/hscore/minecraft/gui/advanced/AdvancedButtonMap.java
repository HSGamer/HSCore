package me.hsgamer.hscore.minecraft.gui.advanced;

import me.hsgamer.hscore.minecraft.gui.GUI;
import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.event.ViewerEvent;
import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import me.hsgamer.hscore.minecraft.gui.button.ActionItem;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import me.hsgamer.hscore.ui.property.Initializable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * An advanced button map that uses {@link Mask}
 */
public class AdvancedButtonMap implements Initializable {
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
    for (Mask mask : masks) {
      Optional<Map<Integer, Button>> buttons = mask.generateButtons(uuid, inventorySize);
      if (!buttons.isPresent()) continue;
      buttons.get().forEach((slot, button) -> {
        if (slot < 0 || slot >= inventorySize.getSize()) {
          return;
        }

        ActionItem actionItem = button.display(uuid);
        if (actionItem == null) {
          return;
        }
        map.computeIfAbsent(slot, s -> new ActionItem()).apply(actionItem);
      });
    }
    return map;
  }

  /**
   * Apply the buttons to the GUI
   *
   * @param uuid the unique id
   * @param gui  the GUI
   */
  public void apply(UUID uuid, GUI gui) {
    Map<Integer, ActionItem> buttons = getButtons(uuid, gui.getInventorySize());
    Map<Integer, Item> items = buttons.entrySet().stream()
      .filter(entry -> entry.getValue().getItem() != null)
      .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getItem()));
    gui.setItems(items);
    gui.setViewerEventConsumer(event -> {
      if (event instanceof ClickEvent) {
        ClickEvent clickEvent = (ClickEvent) event;
        ActionItem actionItem = buttons.get(clickEvent.getSlot());
        if (actionItem == null) return;
        Consumer<ViewerEvent> action = actionItem.getAction();
        if (action == null) return;
        action.accept(event);
      }
    });
  }
}
