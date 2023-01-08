package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.bukkit.gui.button.ButtonMap;
import me.hsgamer.hscore.ui.BaseHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/**
 * The base {@link me.hsgamer.hscore.ui.Holder} for UI in Bukkit
 */
public class GUIHolder extends BaseHolder<GUIDisplay> {
  private final Plugin plugin;
  private boolean removeDisplayOnClose = true;
  private InventoryType inventoryType = InventoryType.CHEST;
  private Function<UUID, String> titleFunction = uuid -> inventoryType.getDefaultTitle();
  private BiFunction<GUIDisplay, UUID, Inventory> inventoryFunction = (display, uuid) -> {
    GUIHolder holder = display.getHolder();
    InventoryType type = holder.getInventoryType();
    int size = holder.getSize(uuid);
    String title = holder.getTitle(uuid);
    return type == InventoryType.CHEST && size > 0
      ? Bukkit.createInventory(display, GUIUtils.normalizeToChestSize(size), title)
      : Bukkit.createInventory(display, type, title);
  };
  private ToIntFunction<UUID> sizeFunction = uuid -> InventoryType.CHEST.getDefaultSize();
  private Predicate<UUID> closeFilter = uuid -> true;
  private ButtonMap buttonMap = uuid -> Collections.emptyMap();

  /**
   * Create a new holder
   *
   * @param plugin the plugin
   */
  public GUIHolder(Plugin plugin) {
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

  /**
   * Get the button map
   *
   * @return the button map
   */
  public ButtonMap getButtonMap() {
    return buttonMap;
  }

  /**
   * Set the button map
   *
   * @param buttonMap the button map
   */
  public void setButtonMap(ButtonMap buttonMap) {
    this.buttonMap = buttonMap;
  }

  /**
   * Get the inventory function
   *
   * @return the inventory function
   */
  public BiFunction<GUIDisplay, UUID, Inventory> getInventoryFunction() {
    return inventoryFunction;
  }

  /**
   * Set the inventory function
   *
   * @param inventoryFunction the inventory function
   */
  public void setInventoryFunction(BiFunction<GUIDisplay, UUID, Inventory> inventoryFunction) {
    this.inventoryFunction = inventoryFunction;
  }

  @Override
  protected GUIDisplay newDisplay(UUID uuid) {
    return new GUIDisplay(uuid, this);
  }

  @Override
  protected void onRemoveDisplay(GUIDisplay display) {
    display.getInventory().getViewers()
      .stream()
      .filter(viewer -> viewer.getUniqueId() != display.getUniqueId())
      .forEach(HumanEntity::closeInventory);
  }

  /**
   * Get the open display of the player
   *
   * @param player the player
   *
   * @return the display
   */
  private Optional<GUIDisplay> getOpenDisplay(HumanEntity player) {
    return Optional.ofNullable(player.getOpenInventory())
      .map(InventoryView::getTopInventory)
      .map(Inventory::getHolder)
      .filter(GUIDisplay.class::isInstance)
      .map(GUIDisplay.class::cast);
  }

  @Override
  public void init() {
    addEventConsumer(InventoryCloseEvent.class, event -> {
      HumanEntity player = event.getPlayer();
      UUID uuid = player.getUniqueId();

      Optional<GUIDisplay> optionalDisplay = getOpenDisplay(player);
      if (!optionalDisplay.isPresent()) {
        return;
      }
      GUIDisplay display = optionalDisplay.get();

      if (!closeFilter.test(uuid)) {
        Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(display.getInventory()));
      } else if (removeDisplayOnClose && display.getUniqueId() == uuid) {
        onOwnerRemoveDisplay(uuid, display, event);
        removeDisplay(uuid);
      }
    });

    addEventConsumer(InventoryOpenEvent.class, this::onOpen);
    addEventConsumer(InventoryClickEvent.class, this::onClick);
    addEventConsumer(InventoryCloseEvent.class, this::onClose);
    addEventConsumer(InventoryDragEvent.class, this::onDrag);

    addEventConsumer(InventoryClickEvent.class, event -> {
      HumanEntity player = event.getWhoClicked();
      UUID uuid = player.getUniqueId();
      getOpenDisplay(player).ifPresent(guiDisplay -> guiDisplay.handleClickEvent(uuid, event));
    });
    buttonMap.init();
  }

  @Override
  public void stop() {
    List<GUIDisplay> list = new ArrayList<>(displayMap.values());
    super.stop();
    list.forEach(guiDisplay -> new ArrayList<>(guiDisplay.getInventory().getViewers()).forEach(HumanEntity::closeInventory));
    buttonMap.stop();
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
   * Handle close event when the owner closes the inventory.
   * Mainly used to clear custom close filters for the viewers of the display.
   *
   * @param uuid    the unique id of the owner
   * @param display the display
   * @param event   the event
   */
  protected void onOwnerRemoveDisplay(UUID uuid, GUIDisplay display, InventoryCloseEvent event) {
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
