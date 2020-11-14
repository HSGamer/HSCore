package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.ui.BaseDisplay;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * The display for {@link GUIHolder}
 */
public class GUIDisplay extends BaseDisplay<GUIHolder> implements InventoryHolder {

  private Inventory inventory;

  /**
   * Create a new display
   *
   * @param uuid   the unique id
   * @param holder the holder
   */
  protected GUIDisplay(UUID uuid, GUIHolder holder) {
    super(uuid, holder);
  }

  @Override
  public void init() {
    if (this.holder.inventoryType == InventoryType.CHEST && this.holder.size > 0) {
      this.inventory = Bukkit.createInventory(this, this.holder.size, this.holder.titleFunction.apply(uuid));
    } else {
      this.inventory = Bukkit.createInventory(this, this.holder.inventoryType, this.holder.titleFunction.apply(uuid));
    }
    update();

    Player player = Bukkit.getPlayer(this.uuid);
    if (player != null) {
      player.openInventory(this.inventory);
    }
  }

  @Override
  public void update() {
    if (inventory == null) {
      return;
    }

    IntStream.range(0, inventory.getSize()).forEach(i ->
      inventory.setItem(i,
        Optional.ofNullable(holder.buttonSlotMap.get(i))
          .map(button -> button.getItemStack(uuid))
          .orElse(null)
      )
    );
  }

  @Override
  public void stop() {
    if (inventory != null) {
      inventory.clear();
    }
  }

  @Override
  public Inventory getInventory() {
    return inventory;
  }
}
