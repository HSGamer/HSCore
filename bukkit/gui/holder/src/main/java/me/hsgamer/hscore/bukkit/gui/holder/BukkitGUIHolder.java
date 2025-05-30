package me.hsgamer.hscore.bukkit.gui.holder;

import me.hsgamer.hscore.bukkit.gui.common.event.BukkitClickEvent;
import me.hsgamer.hscore.bukkit.gui.common.event.BukkitDragEvent;
import me.hsgamer.hscore.bukkit.gui.common.inventory.BukkitInventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import me.hsgamer.hscore.minecraft.gui.holder.GUIHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

/**
 * The {@link GUIHolder} for Bukkit
 */
public class BukkitGUIHolder extends GUIHolder<BukkitInventoryContext> {
  private final Function<BukkitGUIHolder, Inventory> inventoryFunction;
  private boolean moveItemOnBottom = false;
  private boolean cancelDragEvent = true;

  /**
   * Create a new holder
   *
   * @param viewerID          the unique ID of the viewer
   * @param inventoryFunction the function to create the inventory
   */
  public BukkitGUIHolder(UUID viewerID, Function<BukkitGUIHolder, Inventory> inventoryFunction) {
    super(viewerID);
    this.inventoryFunction = inventoryFunction;
  }

  /**
   * Set whether to allow moving items in the bottom inventory
   *
   * @param moveItemOnBottom true to allow
   */
  public void setMoveItemOnBottom(boolean moveItemOnBottom) {
    this.moveItemOnBottom = moveItemOnBottom;
  }

  /**
   * Set whether to cancel the drag event
   *
   * @param cancelDragEvent true to cancel
   */
  public void setCancelDragEvent(boolean cancelDragEvent) {
    this.cancelDragEvent = cancelDragEvent;
  }

  @Override
  protected BukkitInventoryContext createInventoryContext() {
    return new BukkitInventoryContext(getViewerID(), inventoryFunction.apply(this));
  }

  @Override
  protected void setItem(int slot, @Nullable ActionItem item) {
    Object i = item != null ? item.getItem() : null;
    getInventoryContext().getInventory().setItem(slot, i instanceof ItemStack ? (ItemStack) i : null);
  }

  @Override
  public void open(UUID uuid) {
    Player player = Bukkit.getPlayer(uuid);
    if (player != null) {
      player.openInventory(getInventoryContext().getInventory());
    }
  }

  @Override
  public void handleClick(ClickEvent event) {
    if (moveItemOnBottom && event instanceof BukkitClickEvent) {
      InventoryClickEvent clickEvent = ((BukkitClickEvent) event).getEvent();
      if (clickEvent.getClickedInventory() != clickEvent.getInventory()) {
        switch (clickEvent.getAction()) {
          case DROP_ALL_SLOT:
          case DROP_ONE_SLOT:
          case PICKUP_ALL:
          case PICKUP_HALF:
          case PICKUP_ONE:
          case PICKUP_SOME:
          case HOTBAR_MOVE_AND_READD:
          case PLACE_ALL:
          case PLACE_ONE:
          case PLACE_SOME:
          case HOTBAR_SWAP:
          case SWAP_WITH_CURSOR:
            clickEvent.setCancelled(false);
            break;
          default:
            break;
        }
        return;
      }
    }
    super.handleClick(event);
  }

  /**
   * Handle the drag event
   *
   * @param event the event
   */
  public void handleDrag(BukkitDragEvent event) {
    if (cancelDragEvent && event != null) {
      for (int slot : event.getRawSlots()) {
        if (slot < getInventoryContext().getSize()) {
          event.setCancelled(true);
          break;
        }
      }
    }
  }
}
