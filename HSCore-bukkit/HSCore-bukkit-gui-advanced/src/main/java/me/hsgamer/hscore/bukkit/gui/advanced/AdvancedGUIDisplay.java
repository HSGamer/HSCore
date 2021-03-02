package me.hsgamer.hscore.bukkit.gui.advanced;

import me.hsgamer.hscore.bukkit.gui.GUIDisplay;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

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
    this.holder.getMasks().forEach(mask -> mask.generateButtons(this.getUniqueId()).forEach((slot, button) -> {
      if (slot >= size) {
        return;
      }
      ItemStack itemStack = button.getItemStack(uuid);
      if (itemStack == null) {
        return;
      }
      inventory.setItem(slot, itemStack);
      viewedButtons.put(slot, button::handleAction);
    }));

    if (forceUpdate) {
      new ArrayList<>(inventory.getViewers())
        .stream()
        .filter(humanEntity -> humanEntity instanceof Player)
        .forEach(humanEntity -> ((Player) humanEntity).updateInventory());
    }
  }
}
