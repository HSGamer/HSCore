package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.bukkit.gui.event.BukkitDragEvent;
import me.hsgamer.hscore.minecraft.gui.GUIHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * The {@link GUIHolder} for Bukkit
 */
public class BukkitGUIHolder extends GUIHolder<BukkitGUIDisplay> {
  private final Plugin plugin;
  private InventoryType inventoryType = InventoryType.CHEST;
  private int size = InventoryType.CHEST.getDefaultSize();
  private Function<BukkitGUIDisplay, Inventory> inventoryFunction = display -> {
    BukkitGUIHolder holder = display.getHolder();
    InventoryType type = holder.getInventoryType();
    int size = holder.getSize();
    return type == InventoryType.CHEST && size > 0
      ? Bukkit.createInventory(display, BukkitGUIUtils.normalizeToChestSize(size))
      : Bukkit.createInventory(display, type);
  };

  /**
   * Create a new holder
   *
   * @param plugin the plugin
   */
  public BukkitGUIHolder(Plugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Get the plugin
   *
   * @return the plugin
   */
  public Plugin getPlugin() {
    return plugin;
  }

  /**
   * Get the inventory type
   *
   * @return the inventory type
   */
  public InventoryType getInventoryType() {
    return inventoryType;
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
   * Get the size of the inventory
   *
   * @return the size
   */
  public int getSize() {
    return size;
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
   * Get the inventory function
   *
   * @return the inventory function
   */
  public Function<BukkitGUIDisplay, Inventory> getInventoryFunction() {
    return inventoryFunction;
  }

  /**
   * Set the inventory function
   *
   * @param inventoryFunction the inventory function
   */
  public void setInventoryFunction(Function<BukkitGUIDisplay, Inventory> inventoryFunction) {
    this.inventoryFunction = inventoryFunction;
  }

  /**
   * Set the title function
   *
   * @param titleFunction the title function
   */
  public void setTitleFunction(Function<UUID, String> titleFunction) {
    setInventoryFunction(display -> {
      BukkitGUIHolder holder = display.getHolder();
      InventoryType type = holder.getInventoryType();
      int size = holder.getSize();
      String title = titleFunction.apply(display.getUniqueId());
      return type == InventoryType.CHEST && size > 0
        ? Bukkit.createInventory(display, BukkitGUIUtils.normalizeToChestSize(size), title)
        : Bukkit.createInventory(display, type, title);
    });
  }

  /**
   * Set the title
   *
   * @param title the title
   *
   * @see #setTitleFunction(Function)
   */
  public void setTitle(String title) {
    setTitleFunction(uuid -> title);
  }

  @Override
  protected @NotNull BukkitGUIDisplay newDisplay(UUID uuid) {
    return new BukkitGUIDisplay(uuid, this);
  }

  @Override
  public void init() {
    super.init();
    addEventConsumer(BukkitDragEvent.class, this::onDrag);
  }

  @Override
  protected void closeAll(List<BukkitGUIDisplay> displays) {
    displays.forEach(display -> new ArrayList<>(display.getInventory().getViewers()).forEach(HumanEntity::closeInventory));
  }

  /**
   * Handle drag event
   *
   * @param event the event
   */
  protected void onDrag(BukkitDragEvent event) {
    // EMPTY
  }
}
