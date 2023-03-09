package me.hsgamer.hscore.minestom.gui;

import me.hsgamer.hscore.minecraft.gui.InventoryGUIDisplay;
import me.hsgamer.hscore.minecraft.gui.event.CloseEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import me.hsgamer.hscore.minestom.gui.event.MinestomCloseEvent;
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
  }

  @Override
  protected void clearInventory() {
    if (inventory != null) {
      inventory.stop();
    }
  }

  @Override
  protected int getInventorySize() {
    return inventory == null ? 0 : inventory.getSize();
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
  public void scheduleReopen(CloseEvent event) {
    if (event instanceof MinestomCloseEvent minestomCloseEvent) {
      minestomCloseEvent.getEvent().setNewInventory(inventory);
    }
  }

  @Override
  public void open() {
    Player player = MinecraftServer.getConnectionManager().getPlayer(uuid);
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
