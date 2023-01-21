package me.hsgamer.hscore.minecraft.gui;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.IntStream;

public abstract class InventoryGUIDisplay extends GUIDisplay {
  protected InventoryGUIDisplay(@NotNull final UUID uuid, @NotNull final GUIHolder holder) {
    super(uuid, holder);
  }

  protected abstract void initInventory();

  protected abstract void clearInventory();

  protected abstract int getInventorySize();

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
