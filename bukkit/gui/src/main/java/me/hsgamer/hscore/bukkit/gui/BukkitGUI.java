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
  private InventoryType inventoryType = InventoryType.CHEST;
  private int size = InventoryType.CHEST.getDefaultSize();
  private String title = "";
  private Inventory inventory;
  private InventorySize inventorySize;

  /**
   * Create a new UI
   *
   * @param uuid the unique id of the UI
   */
  public BukkitGUI(UUID uuid) {
    super(uuid);
  }

  /**
   * Get the inventory type
   *
   * @return the inventory type
   */
  public InventoryType getInventoryType() {
    return inventoryType;
  }

  /**
   * Set the inventory type
   *
   * @param inventoryType the inventory type
   */
  public void setInventoryType(InventoryType inventoryType) {
    this.inventoryType = inventoryType;
  }

  /**
   * Get the size of the inventory
   *
   * @return the size
   */
  public int getSize() {
    return size;
  }

  /**
   * Set the size of the inventory
   *
   * @param size the size
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * Get the title of the inventory
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Set the title of the inventory
   *
   * @param title the title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  protected void initInventory() {
    inventory = inventoryType == InventoryType.CHEST && size > 0
      ? Bukkit.createInventory(this, normalizeToChestSize(size))
      : Bukkit.createInventory(this, inventoryType);
    inventorySize = new BukkitInventorySize(inventory);
  }

  @Override
  protected void clearInventory() {
    if (inventory != null) {
      inventory.clear();
    }
    inventory = null;
    inventorySize = null;
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
    if (inventorySize == null) {
      throw new IllegalStateException("Inventory size is not initialized");
    }
    return inventorySize;
  }

  @Override
  public void open() {
    Player player = Bukkit.getPlayer(uuid);
    if (player != null) {
      player.openInventory(inventory);
    }
  }

  @Override
  public Inventory getInventory() {
    if (inventory == null) {
      throw new IllegalStateException("Inventory is not initialized");
    }
    return inventory;
  }
}
