package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.bukkit.gui.object.BukkitInventorySize;
import me.hsgamer.hscore.bukkit.gui.object.BukkitItem;
import me.hsgamer.hscore.minecraft.gui.GUI;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static me.hsgamer.hscore.bukkit.gui.BukkitGUIUtils.normalizeToChestSize;

/**
 * The GUI for Bukkit
 */
public class BukkitGUI extends GUI implements InventoryHolder {
  private final Inventory inventory;
  private final InventorySize inventorySize;

  /**
   * Create a GUI
   *
   * @param inventory the inventory
   */
  public BukkitGUI(Inventory inventory) {
    this.inventory = inventory;
    this.inventorySize = new BukkitInventorySize(inventory);
  }

  /**
   * Create a GUI
   *
   * @param inventoryType the inventory type
   * @param size          the size of the inventory if the inventory type is CHEST
   * @param title         the title of the inventory
   */
  public BukkitGUI(InventoryType inventoryType, int size, String title) {
    if (inventoryType == InventoryType.CHEST) {
      this.inventory = Bukkit.createInventory(this, normalizeToChestSize(size), title);
    } else {
      this.inventory = Bukkit.createInventory(this, inventoryType, title);
    }
    this.inventorySize = new BukkitInventorySize(inventory);
  }

  /**
   * Open the inventory for the player
   *
   * @param player the player
   */
  public void open(Player player) {
    player.openInventory(inventory);
  }

  @Override
  protected void setItem(int slot, @Nullable Item item) {
    if (item == null) {
      inventory.setItem(slot, new ItemStack(Material.AIR));
    } else if (item instanceof BukkitItem) {
      inventory.setItem(slot, ((BukkitItem) item).getItemStack());
    }
  }

  @Override
  public InventorySize getInventorySize() {
    return inventorySize;
  }

  @Override
  public void open(UUID uuid) {
    Player player = Bukkit.getPlayer(uuid);
    if (player != null) {
      open(player);
    }
  }

  @Override
  public void stop() {
    super.stop();
    inventory.clear();
  }

  @Override
  public Inventory getInventory() {
    return inventory;
  }
}
