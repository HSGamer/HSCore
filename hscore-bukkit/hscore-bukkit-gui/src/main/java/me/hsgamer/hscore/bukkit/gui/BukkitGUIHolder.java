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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * The {@link GUIHolder} for Bukkit
 */
public class BukkitGUIHolder extends GUIHolder<BukkitGUIDisplay> {
  private final Plugin plugin;
  private InventoryType inventoryType = InventoryType.CHEST;
  private Function<UUID, String> titleFunction = uuid -> inventoryType.getDefaultTitle();
  private ToIntFunction<UUID> sizeFunction = uuid -> InventoryType.CHEST.getDefaultSize();
  private BiFunction<BukkitGUIDisplay, UUID, Inventory> inventoryFunction = (display, uuid) -> {
    BukkitGUIHolder holder = display.getHolder();
    InventoryType type = holder.getInventoryType();
    int size = holder.getSize(uuid);
    String title = holder.getTitle(uuid);
    return type == InventoryType.CHEST && size > 0
      ? Bukkit.createInventory(display, BukkitGUIUtils.normalizeToChestSize(size), title)
      : Bukkit.createInventory(display, type, title);
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
   * Get the title function
   *
   * @return the title function
   */
  public Function<UUID, String> getTitleFunction() {
    return titleFunction;
  }

  /**
   * Set the title function
   *
   * @param titleFunction the title function
   */
  public void setTitleFunction(Function<UUID, String> titleFunction) {
    this.titleFunction = titleFunction;
  }

  /**
   * Get the title for the unique id
   *
   * @param uuid the unique id
   *
   * @return the title
   *
   * @see #getTitleFunction()
   */
  public String getTitle(UUID uuid) {
    return titleFunction.apply(uuid);
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

  /**
   * Get the size function
   *
   * @return the size function
   */
  public ToIntFunction<UUID> getSizeFunction() {
    return sizeFunction;
  }

  /**
   * Set the size function
   *
   * @param sizeFunction the size function
   */
  public void setSizeFunction(ToIntFunction<UUID> sizeFunction) {
    this.sizeFunction = sizeFunction;
  }

  /**
   * Get the size of the inventory for the unique id
   *
   * @param uuid the unique id
   *
   * @return the size
   *
   * @see #getSizeFunction()
   */
  public int getSize(UUID uuid) {
    return sizeFunction.applyAsInt(uuid);
  }

  /**
   * Set the size
   *
   * @param size the size
   *
   * @see #setSizeFunction(ToIntFunction)
   */
  public void setSize(int size) {
    setSizeFunction(uuid -> size);
  }

  /**
   * Get the inventory function
   *
   * @return the inventory function
   */
  public BiFunction<BukkitGUIDisplay, UUID, Inventory> getInventoryFunction() {
    return inventoryFunction;
  }

  /**
   * Set the inventory function
   *
   * @param inventoryFunction the inventory function
   */
  public void setInventoryFunction(BiFunction<BukkitGUIDisplay, UUID, Inventory> inventoryFunction) {
    this.inventoryFunction = inventoryFunction;
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
