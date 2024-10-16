package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.bukkit.gui.object.BukkitInventorySize;
import me.hsgamer.hscore.bukkit.gui.object.BukkitItem;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * The {@link me.hsgamer.hscore.minecraft.gui.GUIDisplay} for Bukkit
 */
public class BukkitGUIDisplay extends InventoryGUIDisplay<BukkitGUIHolder> implements InventoryHolder {
  private Inventory inventory;
  private InventorySize inventorySize;

  /**
   * Create a new display
   *
   * @param uuid   the unique id
   * @param holder the holder
   */
  public BukkitGUIDisplay(UUID uuid, BukkitGUIHolder holder) {
    super(uuid, holder);
  }

  @Override
  protected void initInventory() {
    this.inventory = holder.getInventoryFunction().apply(this);
    this.inventorySize = new BukkitInventorySize(inventory);
  }

  @Override
  protected void clearInventory() {
    if (inventory != null) {
      inventory.clear();
    }
  }

  @Override
  protected InventorySize getInventorySize() {
    return inventorySize;
  }

  @Override
  protected void setButton(int slot, @Nullable Item item) {
    if (item == null) {
      inventory.setItem(slot, new ItemStack(Material.AIR));
    } else if (item instanceof BukkitItem) {
      inventory.setItem(slot, ((BukkitItem) item).getItemStack());
    }
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
    return inventory;
  }
}
