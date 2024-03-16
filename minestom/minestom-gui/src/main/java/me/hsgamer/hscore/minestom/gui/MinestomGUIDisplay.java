package me.hsgamer.hscore.minestom.gui;

import me.hsgamer.hscore.minecraft.gui.InventoryGUIDisplay;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import me.hsgamer.hscore.minestom.gui.inventory.DelegatingInventory;
import me.hsgamer.hscore.minestom.gui.object.MinestomItem;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * The {@link me.hsgamer.hscore.minecraft.gui.GUIDisplay} for Minestom
 */
public class MinestomGUIDisplay extends InventoryGUIDisplay<MinestomGUIHolder> {
  private DelegatingInventory inventory;
  private InventorySize inventorySize;

  /**
   * Create a new display
   *
   * @param uuid   the unique id
   * @param holder the holder
   */
  public MinestomGUIDisplay(@NotNull UUID uuid, @NotNull MinestomGUIHolder holder) {
    super(uuid, holder);
  }

  @Override
  protected void initInventory() {
    this.inventory = new DelegatingInventory(holder.getInventoryType(), holder.getTitleFunction().apply(uuid), this);
    this.inventory.init();
    this.inventorySize = new InventorySize() {
      @Override
      public int getSize() {
        return inventory.getSize();
      }

      @Override
      public int getSlotPerRow() {
        return inventory.getSlotPerRow();
      }
    };
  }

  @Override
  protected void clearInventory() {
    if (inventory != null) {
      inventory.stop();
    }
  }

  @Override
  protected InventorySize getInventorySize() {
    return inventorySize;
  }

  @Override
  protected void setButton(int slot, @Nullable Item item) {
    if (item == null) {
      inventory.setItemStack(slot, ItemStack.AIR);
    } else if (item instanceof MinestomItem minestomItem) {
      inventory.setItemStack(slot, minestomItem.itemStack());
    }
  }

  @Override
  public void open() {
    Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
    if (player != null) {
      player.openInventory(inventory);
    }
  }

  /**
   * Get the inventory of the display
   *
   * @return the inventory
   */
  @Nullable
  public DelegatingInventory getInventory() {
    return inventory;
  }
}
