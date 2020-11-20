package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.ui.BaseHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The UI Holder for Bukkit
 */
public class GUIHolder extends BaseHolder<GUIDisplay> {

  private final Map<Integer, Button> buttonSlotMap = new ConcurrentHashMap<>();
  private final Plugin plugin;
  private final boolean removeDisplayOnClose;
  private InventoryType inventoryType = InventoryType.CHEST;
  private Function<UUID, String> titleFunction = uuid -> inventoryType.getDefaultTitle();
  private int size = InventoryType.CHEST.getDefaultSize();
  private Predicate<UUID> closeFilter = uuid -> true;

  /**
   * Create a new holder
   *
   * @param plugin               the plugin
   * @param removeDisplayOnClose whether the display should be removed on close event
   */
  public GUIHolder(Plugin plugin, boolean removeDisplayOnClose) {
    this.plugin = plugin;
    this.removeDisplayOnClose = removeDisplayOnClose;
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
   * Set the button
   *
   * @param slot   the slot
   * @param button the button
   */
  public void setButton(int slot, Button button) {
    buttonSlotMap.put(slot, button);
    button.init();
  }

  /**
   * Remove the button
   *
   * @param slot the slot
   */
  public void removeButton(int slot) {
    Optional.ofNullable(buttonSlotMap.remove(slot)).ifPresent(Button::stop);
  }

  /**
   * Remove all buttons
   */
  public void removeAllButton() {
    buttonSlotMap.values().forEach(Button::stop);
    buttonSlotMap.clear();
  }

  /**
   * Get the button
   *
   * @param slot the slot
   *
   * @return the button
   */
  public Optional<Button> getButton(int slot) {
    return Optional.ofNullable(buttonSlotMap.get(slot));
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
   * Get the map of buttons
   *
   * @return the map of buttons
   */
  public Map<Integer, Button> getButtonSlotMap() {
    return Collections.unmodifiableMap(buttonSlotMap);
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
   * \
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

    addEventConsumer(InventoryClickEvent.class,
      event -> Optional.ofNullable(buttonSlotMap.get(event.getRawSlot()))
        .ifPresent(button -> button.handleAction(event.getWhoClicked().getUniqueId(), event))
    );
  }

  @Override
  public void stop() {
    removeAllButton();
    List<GUIDisplay> list = new ArrayList<>(displayMap.values());
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
}
