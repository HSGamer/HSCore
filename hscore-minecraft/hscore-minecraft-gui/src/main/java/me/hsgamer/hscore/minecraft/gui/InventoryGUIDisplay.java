package me.hsgamer.hscore.minecraft.gui;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.IntStream;

/**
 * An implementation of {@link GUIDisplay} for Inventory-based GUI
 *
 * @param <H> the type of the holder
 */
public abstract class InventoryGUIDisplay<H extends GUIHolder<?>> extends GUIDisplay<H> {
  /**
   * Create a new display
   *
   * @param uuid   the unique id
   * @param holder the holder
   */
  protected InventoryGUIDisplay(@NotNull UUID uuid, @NotNull H holder) {
    super(uuid, holder);
  }

  /**
   * Initialize the inventory
   */
  protected abstract void initInventory();

  /**
   * Clear the inventory
   */
  protected abstract void clearInventory();

  /**
   * Get the size of the inventory
   *
   * @return the size
   */
  protected abstract int getInventorySize();

  /**
   * Get the title of the inventory
   *
   * @param slot the slot
   * @param item the item
   */
  protected abstract void setButton(int slot, @Nullable Item item);

  @Override
  public void init() {
    initInventory();
    update();
  }

  @Override
  public void stop() {
    clearInventory();
    viewedButtons.clear();
  }

  @Override
  public void update() {
    int size = getInventorySize();
    List<Integer> emptyItemSlots = IntStream.range(0, size).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    List<Integer> emptyActionSlots = IntStream.range(0, size).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    Map<Button, Collection<Integer>> buttonSlots = holder.getButtonMap().getButtons(uuid);
    buttonSlots.forEach((button, slots) -> {
      Item item = button.getItem(uuid);
      if (item == null && !button.forceSetAction(uuid)) {
        return;
      }
      slots.forEach(slot -> {
        if (slot >= size) {
          return;
        }
        if (item != null) {
          setButton(slot, item);
          emptyItemSlots.remove(slot);
        }
        viewedButtons.put(slot, button);
        emptyActionSlots.remove(slot);
      });
    });

    Button defaultButton = holder.getButtonMap().getDefaultButton(uuid);
    Item item = defaultButton.getItem(uuid);
    emptyItemSlots.forEach(slot -> setButton(slot, item));
    emptyActionSlots.forEach(slot -> viewedButtons.put(slot, defaultButton));
  }
}
