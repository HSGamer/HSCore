package me.hsgamer.hscore.bukkit.gui.advanced;

import me.hsgamer.hscore.bukkit.gui.GUIDisplay;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * The display for {@link AdvancedGUIHolder}
 */
public class AdvancedGUIDisplay extends GUIDisplay<AdvancedGUIHolder> {

  protected AdvancedGUIDisplay(UUID uuid, AdvancedGUIHolder holder) {
    super(uuid, holder);
  }

  @Override
  protected Inventory createInventory() {
    return Bukkit.createInventory(this, this.holder.getSize(), this.holder.getTitle(uuid));
  }

  @Override
  public void update() {
    if (inventory == null) {
      return;
    }

    int size = inventory.getSize();
    List<Integer> emptySlots = IntStream.range(0, size).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    this.holder.getMasks().forEach(mask -> mask.generateButtons(this.getUniqueId()).forEach((slot, button) -> {
      if (slot >= size) {
        return;
      }
      ItemStack itemStack = button.getItemStack(uuid);
      if (itemStack == null) {
        return;
      }
      inventory.setItem(slot, itemStack);
      emptySlots.remove(slot);
      viewedButtons.put(slot, button::handleAction);
    }));

    // Clear empty slots
    emptySlots.forEach(slot -> {
      inventory.clear(slot);
      viewedButtons.remove(slot);
    });

    if (forceUpdate) {
      new ArrayList<>(inventory.getViewers())
        .stream()
        .filter(humanEntity -> humanEntity instanceof Player)
        .forEach(humanEntity -> ((Player) humanEntity).updateInventory());
    }
  }
}
