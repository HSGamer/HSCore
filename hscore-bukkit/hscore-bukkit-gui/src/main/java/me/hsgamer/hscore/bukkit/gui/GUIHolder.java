package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.bukkit.gui.button.ButtonMap;
import me.hsgamer.hscore.ui.BaseHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.*;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
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

  @Override
  protected GUIDisplay newDisplay(UUID uuid) {
    return new GUIDisplay(uuid, this);
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

    addEventConsumer(InventoryOpenEvent.class, this::onOpen);
    addEventConsumer(InventoryClickEvent.class, this::onClick);
    addEventConsumer(InventoryCloseEvent.class, this::onClose);
    addEventConsumer(InventoryDragEvent.class, this::onDrag);

    addEventConsumer(InventoryClickEvent.class, event -> {
      UUID uuid = event.getWhoClicked().getUniqueId();
      getDisplay(uuid).ifPresent(guiDisplay -> guiDisplay.handleClickEvent(uuid, event));
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
   * Handle drag event
   *
   * @param event the event
   */
  protected void onDrag(InventoryDragEvent event) {
    // EMPTY
  }
}
