package me.hsgamer.hscore.minestom.gui;

import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.holder.GUIHolder;
import me.hsgamer.hscore.minestom.gui.event.MinestomClickEvent;
import me.hsgamer.hscore.minestom.gui.event.MinestomCloseEvent;
import me.hsgamer.hscore.minestom.gui.event.MinestomOpenEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

/**
 * The {@link GUIHolder} for Minestom
 */
public class MinestomGUIHolder extends GUIHolder<MinestomInventoryContext> {
  private final EventNode<InventoryEvent> eventNode;
  private final Function<UUID, Inventory> inventoryFunction;

  /**
   * Create a new holder
   *
   * @param viewerID          the unique ID of the viewer
   * @param buttonMap         the button map
   * @param inventoryFunction the function to create the inventory
   */
  public MinestomGUIHolder(UUID viewerID, ButtonMap buttonMap, Function<UUID, Inventory> inventoryFunction) {
    super(viewerID, buttonMap);
    this.inventoryFunction = inventoryFunction;
    eventNode = EventNode.event("inventory-" + UUID.randomUUID(), EventFilter.INVENTORY, event -> Objects.equals(event.getInventory(), getInventoryContext().getInventory()));
    eventNode.addListener(InventoryOpenEvent.class, event -> handleOpen(new MinestomOpenEvent(event)));
    eventNode.addListener(InventoryPreClickEvent.class, event -> {
      boolean wasCancelled = event.isCancelled();
      event.setCancelled(true);
      handleClick(new MinestomClickEvent(event));
      if (!wasCancelled && !event.isCancelled()) {
        event.setCancelled(false);
      }
    });
    eventNode.addListener(InventoryCloseEvent.class, event -> handleClose(new MinestomCloseEvent(event)));
  }

  @Override
  protected MinestomInventoryContext createInventoryContext(UUID uuid) {
    return new MinestomInventoryContext(uuid, inventoryFunction.apply(uuid));
  }

  @Override
  protected void setItem(int slot, @Nullable Object item) {
    if (item == null) {
      getInventoryContext().getInventory().setItemStack(slot, net.minestom.server.item.ItemStack.AIR);
    } else if (item instanceof ItemStack itemStack) {
      getInventoryContext().getInventory().setItemStack(slot, itemStack);
    }
  }

  @Override
  public void open(UUID uuid) {
    Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
    if (player != null) {
      player.openInventory(getInventoryContext().getInventory());
    }
  }

  @Override
  public void init() {
    super.init();
    MinecraftServer.getGlobalEventHandler().addChild(eventNode);
  }

  @Override
  public void stop() {
    super.stop();
    MinecraftServer.getGlobalEventHandler().removeChild(eventNode);
  }
}
