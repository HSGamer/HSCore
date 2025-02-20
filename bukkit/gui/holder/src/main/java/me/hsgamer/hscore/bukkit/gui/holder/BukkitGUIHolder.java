package me.hsgamer.hscore.bukkit.gui.holder;

import me.hsgamer.hscore.bukkit.gui.common.event.BukkitClickEvent;
import me.hsgamer.hscore.bukkit.gui.common.event.BukkitDragEvent;
import me.hsgamer.hscore.bukkit.gui.common.inventory.BukkitInventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.common.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.common.event.DragEvent;
import me.hsgamer.hscore.minecraft.gui.holder.GUIHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.BiFunction;

/**
 * The {@link GUIHolder} for Bukkit
 */
public class BukkitGUIHolder extends GUIHolder<BukkitInventoryContext> {
  private final BiFunction<UUID, BukkitGUIHolder, Inventory> inventoryFunction;
  private boolean moveItemOnBottom = false;
  private boolean cancelDragEvent = true;

  /**
   * Create a new holder
   *
   * @param viewerID          the unique ID of the viewer
   * @param buttonMap         the button map
   * @param inventoryFunction the function to create the inventory
   */
  public BukkitGUIHolder(UUID viewerID, ButtonMap buttonMap, BiFunction<UUID, BukkitGUIHolder, Inventory> inventoryFunction) {
    super(viewerID, buttonMap);
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
  protected BukkitInventoryContext createInventoryContext(UUID uuid) {
    Inventory inventory = inventoryFunction.apply(uuid, this);
    return new BukkitInventoryContext(uuid, inventory);
  }

  @Override
  protected void setItem(int slot, @Nullable Object item) {
    if (item == null) {
      getInventoryContext().getInventory().setItem(slot, null);
    } else if (item instanceof ItemStack) {
      getInventoryContext().getInventory().setItem(slot, (ItemStack) item);
    }
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
      if (clickEvent.getClickedInventory() != clickEvent.getInventory())
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
    }
    super.handleClick(event);
  }

  @Override
  public void handleDrag(DragEvent event) {
    if (cancelDragEvent && event instanceof BukkitDragEvent) {
      InventoryDragEvent dragEvent = ((BukkitDragEvent) event).getEvent();
      for (int slot : dragEvent.getRawSlots()) {
        if (slot < dragEvent.getInventory().getSize()) {
          event.setCancelled(true);
          break;
        }
      }
    }
    super.handleDrag(event);
  }
}
