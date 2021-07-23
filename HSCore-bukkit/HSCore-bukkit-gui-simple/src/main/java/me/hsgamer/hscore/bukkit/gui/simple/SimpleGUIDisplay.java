package me.hsgamer.hscore.bukkit.gui.simple;

import me.hsgamer.hscore.bukkit.gui.GUIDisplay;
import me.hsgamer.hscore.bukkit.gui.button.Button;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * The display for {@link SimpleGUIHolder}
 */
public class SimpleGUIDisplay extends GUIDisplay<SimpleGUIHolder> {

  protected SimpleGUIDisplay(UUID uuid, SimpleGUIHolder holder) {
    super(uuid, holder);
  }

  @Override
  protected Inventory createInventory() {
    InventoryType type = this.holder.getInventoryType();
    int size = this.holder.getSize(uuid);
    String title = this.holder.getTitle(uuid);
    return type == InventoryType.CHEST && size > 0
      ? Bukkit.createInventory(this, size, title)
      : Bukkit.createInventory(this, type, title);
  }

  @Override
  public void update() {
    if (inventory == null) {
      return;
    }

    int size = inventory.getSize();
    List<Integer> emptyItemSlots = IntStream.range(0, size).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    List<Integer> emptyActionSlots = IntStream.range(0, size).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    this.holder.getButtonSlotMap().forEach((button, slots) -> {
      ItemStack itemStack = button.getItemStack(uuid);
      if (itemStack == null && !button.forceSetAction(uuid)) {
        return;
      }
      slots.forEach(slot -> {
        if (slot >= size) {
          return;
        }
        if (itemStack != null) {
          inventory.setItem(slot, itemStack);
          emptyItemSlots.remove(slot);
        }
        viewedButtons.put(slot, button::handleAction);
        emptyActionSlots.remove(slot);
      });
    });

    Button defaultButton = this.holder.getDefaultButton();
    ItemStack itemStack = defaultButton.getItemStack(uuid);
    emptyItemSlots.forEach(slot -> inventory.setItem(slot, itemStack));
    emptyActionSlots.forEach(slot -> viewedButtons.put(slot, defaultButton::handleAction));

    if (forceUpdate) {
      new ArrayList<>(inventory.getViewers())
        .stream()
        .filter(Player.class::isInstance)
        .forEach(humanEntity -> ((Player) humanEntity).updateInventory());
    }
  }
}
