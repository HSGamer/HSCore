package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.ui.BaseDisplay;
import me.hsgamer.hscore.ui.Holder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * The base {@link me.hsgamer.hscore.ui.Display} for UI in Bukkit
 *
 * @param <H> the type of {@link Holder}
 */
public abstract class GUIDisplay<H extends GUIHolder<?>> extends BaseDisplay<H> implements InventoryHolder {
  protected final Map<Integer, BiConsumer<UUID, InventoryClickEvent>> viewedButtons = new ConcurrentHashMap<>();
  protected Inventory inventory;
  protected boolean forceUpdate = false;

  /**
   * Create a new display
   *
   * @param uuid   the unique id
   * @param holder the holder
   */
  protected GUIDisplay(UUID uuid, H holder) {
    super(uuid, holder);
  }

  /**
   * Should the display force the viewers to update their inventory
   *
   * @return true if it should
   */
  public boolean isForceUpdate() {
    return forceUpdate;
  }

  /**
   * Should the display force the viewers to update their inventory
   *
   * @param forceUpdate true to force them
   *
   * @return {@code this} for builder chain
   */
  public GUIDisplay<?> setForceUpdate(boolean forceUpdate) {
    this.forceUpdate = forceUpdate;
    return this;
  }

  /**
   * Handle the click event
   *
   * @param uuid  the unique id
   * @param event the click event
   */
  public void handleClickEvent(UUID uuid, InventoryClickEvent event) {
    Optional.ofNullable(viewedButtons.get(event.getRawSlot())).ifPresent(consumer -> consumer.accept(uuid, event));
  }

  /**
   * Create the inventory
   *
   * @return the inventory
   */
  protected abstract Inventory createInventory();

  @Override
  public void init() {
    this.inventory = createInventory();
    update();

    Player player = Bukkit.getPlayer(this.uuid);
    if (player != null) {
      player.openInventory(this.inventory);
    }
  }

  @Override
  public void stop() {
    if (inventory != null) {
      inventory.clear();
    }
    viewedButtons.clear();
  }

  @Override
  public Inventory getInventory() {
    return inventory;
  }
}
