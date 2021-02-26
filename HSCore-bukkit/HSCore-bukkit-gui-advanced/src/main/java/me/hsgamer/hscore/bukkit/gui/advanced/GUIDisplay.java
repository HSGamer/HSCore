package me.hsgamer.hscore.bukkit.gui.advanced;

import me.hsgamer.hscore.ui.BaseDisplay;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * The display for {@link GUIHolder}
 */
public class GUIDisplay extends BaseDisplay<GUIHolder> implements InventoryHolder {
  private final Map<Integer, BiConsumer<UUID, InventoryClickEvent>> viewedButtons = new ConcurrentHashMap<>();
  private Inventory inventory;
  private boolean forceUpdate = false;

  /**
   * Create a new display
   *
   * @param uuid   the unique id
   * @param holder the holder
   */
  protected GUIDisplay(UUID uuid, GUIHolder holder) {
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
  public GUIDisplay setForceUpdate(boolean forceUpdate) {
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

  @Override
  public void init() {
    this.inventory = Bukkit.createInventory(this, this.holder.getSize(), this.holder.getTitle(uuid));
    update();

    Player player = Bukkit.getPlayer(this.uuid);
    if (player != null) {
      player.openInventory(this.inventory);
    }
  }

  @Override
  public void update() {
    if (inventory == null) {
      return;
    }

    int size = inventory.getSize();
    this.holder.getMasks().forEach(mask -> mask.generateButtons(this.getUniqueId()).forEach((slot, button) -> {
      if (slot >= size) {
        return;
      }
      ItemStack itemStack = button.getItemStack(uuid);
      if (itemStack == null) {
        return;
      }
      inventory.setItem(slot, itemStack);
      viewedButtons.put(slot, button::handleAction);
    }));

    if (forceUpdate) {
      new ArrayList<>(inventory.getViewers())
        .stream()
        .filter(humanEntity -> humanEntity instanceof Player)
        .forEach(humanEntity -> ((Player) humanEntity).updateInventory());
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
