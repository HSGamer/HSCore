package me.hsgamer.hscore.minestom.gui;

import me.hsgamer.hscore.minecraft.gui.GUI;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import me.hsgamer.hscore.minestom.gui.event.MinestomCloseEvent;
import me.hsgamer.hscore.minestom.gui.inventory.DelegatingInventory;
import me.hsgamer.hscore.minestom.gui.object.MinestomInventorySize;
import me.hsgamer.hscore.minestom.gui.object.MinestomItem;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Predicate;

/**
 * The GUI for Minestom
 */
public class MinestomGUI extends GUI {
  private final DelegatingInventory inventory;
  private final InventorySize inventorySize;
  private Predicate<UUID> closePredicate = uuid -> true;

  public MinestomGUI(InventoryType inventoryType, Component title) {
    this.inventory = new DelegatingInventory(inventoryType, title, this);
    this.inventorySize = new MinestomInventorySize(inventory);
  }

  /**
   * Set the close predicate
   *
   * @param closePredicate the close predicate
   */
  public void setClosePredicate(@NotNull Predicate<UUID> closePredicate) {
    this.closePredicate = closePredicate;
  }

  /**
   * Open the GUI for the player
   *
   * @param player the player
   */
  public void open(Player player) {
    player.openInventory(inventory);
  }

  @Override
  public void init() {
    inventory.init();
    addEventConsumer(MinestomCloseEvent.class, event -> {
      UUID uuid = event.getViewerID();
      if (closePredicate != null && !closePredicate.test(uuid)) {
        event.setRemoveDisplay(false);
        InventoryCloseEvent inventoryCloseEvent = event.getEvent();
        inventoryCloseEvent.setNewInventory(inventoryCloseEvent.getInventory());
      }
    });
    super.init();
  }

  @Override
  public void stop() {
    super.stop();
    inventory.stop();
  }

  @Override
  public void setItem(int slot, @Nullable Item item) {
    if (item == null) {
      inventory.setItemStack(slot, net.minestom.server.item.ItemStack.AIR);
    } else if (item instanceof MinestomItem minestomItem) {
      inventory.setItemStack(slot, minestomItem.itemStack());
    }
  }

  @Override
  public InventorySize getInventorySize() {
    return inventorySize;
  }

  @Override
  public void open(UUID uuid) {
    Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
    if (player != null) {
      open(player);
    }
  }
}
