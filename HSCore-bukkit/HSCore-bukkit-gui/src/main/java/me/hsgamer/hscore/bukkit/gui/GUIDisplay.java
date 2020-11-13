package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.ui.Display;
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
public class GUIDisplay implements Display, InventoryHolder {

  private final UUID uuid;
  private final GUIHolder holder;
  private Inventory inventory;

  protected GUIDisplay(UUID uuid, GUIHolder holder) {
    this.uuid = uuid;
    this.holder = holder;
  }

  @Override
  public void init() {
    String title = this.holder.title != null ? this.holder.title : this.holder.inventoryType.getDefaultTitle();

    if (this.holder.inventoryType == InventoryType.CHEST && this.holder.size > 0) {
      this.inventory = Bukkit.createInventory(this, this.holder.size, title);
    } else {
      this.inventory = Bukkit.createInventory(this, this.holder.inventoryType, title);
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
  public GUIHolder getHolder() {
    return this.holder;
  }

  @Override
  public UUID getUniqueId() {
    return this.uuid;
  }

  @Override
  public Inventory getInventory() {
    return inventory;
  }
}
