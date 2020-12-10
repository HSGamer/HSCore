package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.ui.BaseDisplay;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    if (this.holder.getInventoryType() == InventoryType.CHEST && this.holder.getSize() > 0) {
      this.inventory = Bukkit.createInventory(this, this.holder.getSize(), this.holder.getTitle(uuid));
    } else {
      this.inventory = Bukkit.createInventory(this, this.holder.getInventoryType(), this.holder.getTitle(uuid));
    }
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

    List<Integer> emptySlots = IntStream.range(0, inventory.getSize()).boxed().collect(Collectors.toList());
    this.holder.getButtonSlotMap().forEach((button, slots) -> {
      ItemStack itemStack = button.getItemStack(uuid);
      if (itemStack != null) {
        slots.forEach(slot -> {
          inventory.setItem(slot, itemStack);
          emptySlots.remove(slot);
          viewedButtons.put(slot, button::handleAction);
        });
      }
    });

    Button defaultButton = this.holder.getDefaultButton();
    ItemStack itemStack = defaultButton.getItemStack(uuid);
    if (itemStack != null) {
      emptySlots.forEach(slot -> {
        inventory.setItem(slot, itemStack);
        viewedButtons.put(slot, defaultButton::handleAction);
      });
    }

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
