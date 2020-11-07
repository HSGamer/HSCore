package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.ui.BaseHolder;
import me.hsgamer.hscore.ui.Updatable;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * The UI Holder for Bukkit
 */
public class GUIHolder extends BaseHolder<GUIDisplay> {

  protected final Map<Integer, Button> buttonSlotMap = new HashMap<>();
  protected String title;
  protected InventoryType inventoryType = InventoryType.CHEST;
  protected int size = InventoryType.CHEST.getDefaultSize();
  protected Predicate<UUID> closeFilter = uuid -> true;

  /**
   * Create a new holder
   *
   * @param plugin               the plugin
   * @param removeDisplayOnClose whether the display should be removed on close event
   */
  public GUIHolder(Plugin plugin, boolean removeDisplayOnClose) {
    addEventConsumer(InventoryCloseEvent.class, event -> {
      HumanEntity player = event.getPlayer();
      UUID uuid = player.getUniqueId();
      if (!closeFilter.test(uuid)) {
        getDisplay(uuid).ifPresent(guiDisplay -> Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(guiDisplay.getInventory())));
      } else if (removeDisplayOnClose) {
        removeDisplay(uuid);
      }
    });

    addEventConsumer(InventoryOpenEvent.class, this::onOpen);
    addEventConsumer(InventoryClickEvent.class, this::onClick);
    addEventConsumer(InventoryCloseEvent.class, this::onClose);

    addEventConsumer(InventoryClickEvent.class,
      event -> Optional.ofNullable(buttonSlotMap.get(event.getRawSlot()))
        .ifPresent(button -> button.handleAction(event.getWhoClicked().getUniqueId(), event))
    );
  }

  /**
   * Create a new holder
   *
   * @param plugin the plugin
   */
  public GUIHolder(Plugin plugin) {
    this(plugin, true);
  }

  /**
   * Set the title
   *
   * @param title the title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Set the inventory type
   *
   * @param inventoryType the inventory type
   */
  public void setInventoryType(InventoryType inventoryType) {
    this.inventoryType = inventoryType;
  }

  /**
   * Set the close filter
   *
   * @param closeFilter the close filter
   */
  public void setCloseFilter(Predicate<UUID> closeFilter) {
    this.closeFilter = closeFilter;
  }

  /**
   * Set the size
   *
   * @param size the size
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * Set the button
   *
   * @param slot   the slot
   * @param button the button
   */
  public void setButton(int slot, Button button) {
    buttonSlotMap.put(slot, button);

    // Updatable Buttons
    if (button instanceof Updatable) {
      ((Updatable) button).initUpdate();
    }
  }

  /**
   * Remove the button
   *
   * @param slot the slot
   */
  public void removeButton(int slot) {
    Optional.ofNullable(buttonSlotMap.remove(slot))
      .filter(button -> button instanceof Updatable)
      .ifPresent(button -> ((Updatable) button).stopUpdate());
  }

  /**
   * Get the button
   *
   * @param slot the slot
   *
   * @return the button
   */
  public Button getButton(int slot) {
    return buttonSlotMap.get(slot);
  }

  @Override
  protected GUIDisplay newDisplay(UUID uuid) {
    return new GUIDisplay(uuid, this);
  }

  /**
   * Handle open event
   *
   * @param event the event
   */
  protected void onOpen(InventoryOpenEvent event) {
    // EMPTY
  }

  /**
   * Handle click event
   *
   * @param event the event
   */
  protected void onClick(InventoryClickEvent event) {
    // EMPTY
  }

  /**
   * Handle close event
   *
   * @param event the event
   */
  protected void onClose(InventoryCloseEvent event) {
    // EMPTY
  }
}
