package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.bukkit.gui.object.BukkitInventorySize;
import me.hsgamer.hscore.bukkit.gui.object.BukkitItem;
import me.hsgamer.hscore.minecraft.gui.GUI;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * The GUI for Bukkit
 */
public class BukkitGUI extends GUI {
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
   * Open the inventory for the player
   *
   * @param player the player
   */
  public void open(Player player) {
    player.openInventory(inventory);
  }

  /**
   * Get the inventory of the GUI
   *
   * @return the inventory
   */
  public Inventory getInventory() {
    return inventory;
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
}
