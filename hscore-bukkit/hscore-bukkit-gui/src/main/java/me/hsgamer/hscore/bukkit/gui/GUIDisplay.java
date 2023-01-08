package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.ui.BaseDisplay;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

/**
 * The base {@link me.hsgamer.hscore.ui.Display} for UI in Bukkit
 */
public class GUIDisplay extends BaseDisplay<GUIHolder> implements InventoryHolder {
  private final Map<Integer, Button> viewedButtons = new ConcurrentHashMap<>();
  private Inventory inventory;
  private boolean forceUpdate = false;

  /**
   * Create a new display
   *
   * @param uuid   the unique id
   * @param holder the holder
   */
  public GUIDisplay(UUID uuid, GUIHolder holder) {
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
    Optional.ofNullable(viewedButtons.get(event.getRawSlot())).ifPresent(button -> button.handleAction(uuid, event));
  }

  /**
   * Open the display for the player
   *
   * @param player the player
   */
  public void open(Player player) {
    player.openInventory(inventory);
  }

  /**
   * Open the display for the owner of this display
   */
  public void open() {
    Player player = Bukkit.getPlayer(uuid);
    if (player != null) {
      open(player);
    }
  }

  @Override
  public void init() {
    this.inventory = holder.getInventoryFunction().apply(this, uuid);
    update();
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

  @Override
  public void update() {
    if (inventory == null) {
      return;
    }

    int size = inventory.getSize();
    List<Integer> emptyItemSlots = IntStream.range(0, size).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    List<Integer> emptyActionSlots = IntStream.range(0, size).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    Map<Button, List<Integer>> buttonSlots = holder.getButtonMap().getButtons(uuid);
    buttonSlots.forEach((button, slots) -> {
      ItemStack itemStack = button.getItemStack(uuid);
      if (itemStack == null && !button.forceSetAction(uuid)) {
        return;
      }
      slots.forEach(slot -> {
        if (slot >= size) {
          return;
        }
        if (itemStack != null) {
          inventory.setItem(slot, itemStack);
          emptyItemSlots.remove(slot);
        }
        viewedButtons.put(slot, button);
        emptyActionSlots.remove(slot);
      });
    });

    Button defaultButton = holder.getButtonMap().getDefaultButton(uuid);
    ItemStack itemStack = defaultButton.getItemStack(uuid);
    emptyItemSlots.forEach(slot -> inventory.setItem(slot, itemStack));
    emptyActionSlots.forEach(slot -> viewedButtons.put(slot, defaultButton));

    if (forceUpdate) {
      new ArrayList<>(inventory.getViewers())
        .stream()
        .filter(Player.class::isInstance)
        .forEach(humanEntity -> ((Player) humanEntity).updateInventory());
    }
  }
}
