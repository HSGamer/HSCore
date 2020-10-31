package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.ui.Display;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

// TODO: finish the methods, init the inventory
public class GUIDisplay implements Display, InventoryHolder {

  private final UUID uuid;
  private final GUIHolder holder;

  protected GUIDisplay(UUID uuid, GUIHolder holder) {
    this.uuid = uuid;
    this.holder = holder;
  }

  @Override
  public void init() {

  }

  @Override
  public void update() {

  }

  @Override
  public void close() {

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
    return null;
  }
}
