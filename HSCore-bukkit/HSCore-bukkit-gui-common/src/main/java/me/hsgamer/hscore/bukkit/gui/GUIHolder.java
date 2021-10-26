package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.ui.BaseHolder;
import me.hsgamer.hscore.ui.Display;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.*;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/**
 * The base {@link me.hsgamer.hscore.ui.Holder} for UI in Bukkit
 *
 * @param <D> The type of {@link Display}
 */
public abstract class GUIHolder<D extends GUIDisplay<?>> extends BaseHolder<D> {
  protected final Plugin plugin;
  protected boolean removeDisplayOnClose = true;
  protected boolean allowMoveItemOnBottom = true;
  protected boolean allowDragEvent = false;
  protected InventoryType inventoryType = InventoryType.CHEST;
  protected Function<UUID, String> titleFunction = uuid -> inventoryType.getDefaultTitle();
  protected ToIntFunction<UUID> sizeFunction = uuid -> InventoryType.CHEST.getDefaultSize();
  protected Predicate<UUID> closeFilter = uuid -> true;

  /**
   * Create a new holder
   *
   * @param plugin               the plugin
   * @param removeDisplayOnClose whether the display should be removed on close event
   *
   * @deprecated use {@link #setRemoveDisplayOnClose(boolean)} before {@link #init()}
   */
  @Deprecated
  protected GUIHolder(Plugin plugin, boolean removeDisplayOnClose) {
    this(plugin);
    setRemoveDisplayOnClose(removeDisplayOnClose);
  }

  /**
   * Create a new holder
   *
   * @param plugin the plugin
   */
  protected GUIHolder(Plugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Get the plugin
   *
   * @return the plugin
   */
  public Plugin getPlugin() {
    return this.plugin;
  }

  /**
   * Check if the holder should remove the display on its close
   *
   * @return true if it should
   */
  public boolean isRemoveDisplayOnClose() {
    return removeDisplayOnClose;
  }

  /**
   * Set that the display should be removed on close event
   *
   * @param removeDisplayOnClose whether the display should be removed on close event
   */
  public void setRemoveDisplayOnClose(boolean removeDisplayOnClose) {
    this.removeDisplayOnClose = removeDisplayOnClose;
  }

  /**
   * Set that the holder should not cancel the click event on bottom inventory
   *
   * @return true if it should
   */
  public boolean isAllowMoveItemOnBottom() {
    return allowMoveItemOnBottom;
  }

  /**
   * Check if the holder should not cancel the click event on bottom inventory
   *
   * @param allowMoveItemOnBottom whether the holder should not cancel the click event on bottom inventory
   */
  public void setAllowMoveItemOnBottom(boolean allowMoveItemOnBottom) {
    this.allowMoveItemOnBottom = allowMoveItemOnBottom;
  }

  /**
   * Check if the holder allows drag event
   *
   * @return true if it does
   */
  public boolean isAllowDragEvent() {
    return allowDragEvent;
  }

  /**
   * Set that the holder allows drag event
   *
   * @param allowDragEvent whether the holder allows drag event
   */
  public void setAllowDragEvent(boolean allowDragEvent) {
    this.allowDragEvent = allowDragEvent;
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
   * Get the close filter
   *
   * @return the close filter
   */
  public Predicate<UUID> getCloseFilter() {
    return closeFilter;
  }

  /**
   * Set the close filter
   *
   * @param closeFilter the close filter
   */
  public void setCloseFilter(Predicate<UUID> closeFilter) {
    this.closeFilter = closeFilter;
  }

  @Override
  public void init() {
    addEventConsumer(InventoryCloseEvent.class, event -> {
      HumanEntity player = event.getPlayer();
      UUID uuid = player.getUniqueId();
      if (!closeFilter.test(uuid)) {
        getDisplay(uuid).ifPresent(guiDisplay -> Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(guiDisplay.getInventory())));
      } else if (removeDisplayOnClose) {
        removeDisplay(uuid);
      }
    });

    if (allowMoveItemOnBottom) {
      addEventConsumer(InventoryClickEvent.class, event -> {
        if (event.getClickedInventory() == event.getInventory()) {
          return;
        }
        switch (event.getAction()) {
          case DROP_ALL_SLOT:
          case DROP_ONE_SLOT:
          case PICKUP_ALL:
          case PICKUP_HALF:
          case PICKUP_ONE:
          case PICKUP_SOME:
          case HOTBAR_MOVE_AND_READD:
          case PLACE_ALL:
          case PLACE_ONE:
          case PLACE_SOME:
          case HOTBAR_SWAP:
          case SWAP_WITH_CURSOR:
            event.setCancelled(false);
            break;
          default:
            break;
        }
      });
    }
    if (!allowDragEvent) {
      addEventConsumer(InventoryDragEvent.class, event -> {
        for (int slot : event.getRawSlots()) {
          if (slot < event.getInventory().getSize()) {
            event.setCancelled(true);
            break;
          }
        }
      });
    }

    addEventConsumer(InventoryOpenEvent.class, this::onOpen);
    addEventConsumer(InventoryClickEvent.class, this::onClick);
    addEventConsumer(InventoryCloseEvent.class, this::onClose);
    addEventConsumer(InventoryDragEvent.class, this::onDrag);

    addEventConsumer(InventoryClickEvent.class, event -> {
      UUID uuid = event.getWhoClicked().getUniqueId();
      getDisplay(uuid).ifPresent(guiDisplay -> guiDisplay.handleClickEvent(uuid, event));
    });
  }

  @Override
  public void stop() {
    List<D> list = new ArrayList<>(displayMap.values());
    super.stop();
    list.forEach(guiDisplay -> new ArrayList<>(guiDisplay.getInventory().getViewers()).forEach(HumanEntity::closeInventory));
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

  /**
   * Handle drag event
   *
   * @param event the event
   */
  protected void onDrag(InventoryDragEvent event) {
    // EMPTY
  }
}
