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
  private InventoryType inventoryType = InventoryType.CHEST_3_ROW;
  private Component title = Component.empty();
  private Predicate<UUID> closePredicate = uuid -> true;
  private DelegatingInventory inventory;
  private InventorySize inventorySize;

  /**
   * Create a new UI
   *
   * @param uuid the unique id of the UI
   */
  public MinestomGUI(UUID uuid) {
    super(uuid);
  }

  /**
   * Get the inventory type
   *
   * @return the inventory type
   */
  @NotNull
  public InventoryType getInventoryType() {
    return inventoryType;
  }

  /**
   * Set the inventory type
   *
   * @param inventoryType the inventory type
   */
  public void setInventoryType(@NotNull InventoryType inventoryType) {
    this.inventoryType = inventoryType;
  }

  /**
   * Get the title
   *
   * @return the title
   */
  @NotNull
  public Component getTitle() {
    return title;
  }

  /**
   * Set the title
   *
   * @param title the title
   */
  public void setTitle(@NotNull Component title) {
    this.title = title;
  }

  /**
   * Get the close predicate
   *
   * @return the close predicate
   */
  @NotNull
  public Predicate<UUID> getClosePredicate() {
    return closePredicate;
  }

  /**
   * Set the close predicate
   *
   * @param closePredicate the close predicate
   */
  public void setClosePredicate(@NotNull Predicate<UUID> closePredicate) {
    this.closePredicate = closePredicate;
  }

  @Override
  protected void initInventory() {
    this.inventory = new DelegatingInventory(inventoryType, title, this);
    this.inventory.init();
    this.inventorySize = new MinestomInventorySize(inventory);
  }

  @Override
  public void init() {
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
  protected void clearInventory() {
    if (inventory != null) {
      inventory.stop();
    }
  }

  @Override
  protected void setItem(int slot, @Nullable Item item) {
    if (item == null) {
      inventory.setItemStack(slot, net.minestom.server.item.ItemStack.AIR);
    } else if (item instanceof MinestomItem minestomItem) {
      inventory.setItemStack(slot, minestomItem.itemStack());
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
    Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
    if (player != null) {
      player.openInventory(inventory);
    }
  }
}
