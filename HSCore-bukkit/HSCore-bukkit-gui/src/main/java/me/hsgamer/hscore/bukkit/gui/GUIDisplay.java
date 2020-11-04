package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.ui.Display;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Optional;
import java.util.UUID;

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
    if (player != null && player.isOnline()) {
      player.openInventory(this.inventory);
    }
  }

  @Override
  public void update() {
    for (int i = 0; i < inventory.getSize(); i++) {
      inventory.setItem(i,
        Optional.ofNullable(holder.buttonSlotMap.get(i))
          .map(button -> button.getItemStack(uuid))
          .orElse(null)
      );
    }
  }

  @Override
  public void close() {
    inventory.getViewers().forEach(HumanEntity::closeInventory);
    inventory.clear();
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
