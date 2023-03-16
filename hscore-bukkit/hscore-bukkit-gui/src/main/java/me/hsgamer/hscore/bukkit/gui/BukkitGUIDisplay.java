package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.bukkit.gui.object.BukkitItem;
import me.hsgamer.hscore.minecraft.gui.InventoryGUIDisplay;
import me.hsgamer.hscore.minecraft.gui.event.CloseEvent;
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
  private String title;
  private int size;
  private Inventory inventory;

  /**
   * Create a new display
   *
   * @param uuid   the unique id
   * @param holder the holder
   */
  public BukkitGUIDisplay(UUID uuid, BukkitGUIHolder holder) {
    super(uuid, holder);
  }

  private Inventory newInventory(UUID uuid) {
    return holder.getInventoryFactory().createInventory(this, uuid, size, title);
  }

  @Override
  protected void initInventory() {
    this.title = holder.getTitleFunction().apply(uuid);
    this.size = holder.getSizeFunction().applyAsInt(uuid);
    this.inventory = newInventory(uuid);
  }

  @Override
  protected void clearInventory() {
    if (inventory != null) {
      inventory.clear();
    }
  }

  @Override
  protected int getInventorySize() {
    return inventory != null ? inventory.getSize() : 0;
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
  public void scheduleReopen(CloseEvent event) {
    Player player = Bukkit.getPlayer(uuid);
    if (player != null) {
      Bukkit.getScheduler().runTask(holder.getPlugin(), () -> player.openInventory(inventory));
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
  public void update() {
    Inventory currentInventory = inventory;
    String newTitle = holder.getTitleFunction().apply(uuid);
    int newSize = holder.getSizeFunction().applyAsInt(uuid);
    if (!newTitle.equals(title) || newSize != size) {
      this.title = newTitle;
      this.size = newSize;
      inventory = newInventory(uuid);
    }

    super.update();

    if (currentInventory != inventory) {
      Player player = Bukkit.getPlayer(uuid);
      if (player != null && player.getOpenInventory().getTopInventory().equals(currentInventory)) {
        player.openInventory(inventory);
      }
    }
  }

  @Override
  public Inventory getInventory() {
    return inventory;
  }
}
