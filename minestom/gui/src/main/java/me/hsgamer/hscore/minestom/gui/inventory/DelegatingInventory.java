package me.hsgamer.hscore.minestom.gui.inventory;

import me.hsgamer.hscore.minestom.gui.MinestomGUI;
import me.hsgamer.hscore.minestom.gui.event.MinestomClickEvent;
import me.hsgamer.hscore.minestom.gui.event.MinestomCloseEvent;
import me.hsgamer.hscore.minestom.gui.event.MinestomOpenEvent;
import me.hsgamer.hscore.ui.property.Initializable;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * A custom inventory used by {@link MinestomGUI}
 */
public class DelegatingInventory extends Inventory implements Initializable {
  private final EventNode<InventoryEvent> eventNode;
  private final MinestomGUI gui;

  /**
   * Create a new inventory
   *
   * @param inventoryType the inventory type
   * @param title         the title
   * @param gui           the gui
   */
  public DelegatingInventory(@NotNull InventoryType inventoryType, @NotNull Component title, @NotNull MinestomGUI gui) {
    super(inventoryType, title);
    this.gui = gui;
    eventNode = EventNode.event("inventory-" + UUID.randomUUID(), EventFilter.INVENTORY, event -> Objects.equals(event.getInventory(), this));
  }

  @Override
  public void init() {
    eventNode.addListener(InventoryOpenEvent.class, event -> gui.handleEvent(new MinestomOpenEvent(event)));
    eventNode.addListener(InventoryPreClickEvent.class, event -> {
      boolean wasCancelled = event.isCancelled();
      event.setCancelled(true);
      gui.handleEvent(new MinestomClickEvent(event));
      if (!wasCancelled && !event.isCancelled()) {
        event.setCancelled(false);
      }
    });
    eventNode.addListener(InventoryCloseEvent.class, event -> gui.handleEvent(new MinestomCloseEvent(event)));
    MinecraftServer.getGlobalEventHandler().addChild(eventNode);
  }

  @Override
  public void stop() {
    clear();
    MinecraftServer.getGlobalEventHandler().removeChild(eventNode);
  }

  /**
   * Get the event node related to this inventory
   *
   * @return the event node
   */
  public EventNode<InventoryEvent> getEventNode() {
    return eventNode;
  }

  /**
   * Get the gui related to this inventory
   *
   * @return the gui
   */
  public MinestomGUI getGUI() {
    return gui;
  }
}
