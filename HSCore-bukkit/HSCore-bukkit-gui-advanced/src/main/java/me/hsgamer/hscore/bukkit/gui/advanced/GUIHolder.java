package me.hsgamer.hscore.bukkit.gui.advanced;

import me.hsgamer.hscore.bukkit.gui.mask.Mask;
import me.hsgamer.hscore.ui.BaseHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The advanced UI Holder for Bukkit (Only accepts {@link InventoryType#CHEST}
 */
public class GUIHolder extends BaseHolder<GUIDisplay> {
  private final List<Mask> masks = new LinkedList<>();
  private final Plugin plugin;
  private final boolean removeDisplayOnClose;
  private Function<UUID, String> titleFunction = uuid -> InventoryType.CHEST.getDefaultTitle();
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
   * Add a mask
   *
   * @param mask the mask
   */
  public void addMask(Mask mask) {
    masks.add(mask);
  }

  /**
   * Remove masks by name
   *
   * @param name the name of the mask
   */
  public void removeMask(String name) {
    masks.removeIf(mask -> mask.getName().equals(name));
  }

  /**
   * Remove all masks
   *
   * @return the removed masks
   */
  public Collection<Mask> removeAllMasks() {
    List<Mask> removedMasks = new LinkedList<>(this.masks);
    masks.clear();
    return removedMasks;
  }

  /**
   * Get masks by name
   *
   * @param name the name of the mask
   *
   * @return the list of masks
   */
  public List<Mask> getMasks(String name) {
    return masks.parallelStream().filter(mask -> mask.getName().equals(name)).collect(Collectors.toList());
  }

  /**
   * Get all masks
   *
   * @return the list of all masks
   */
  public List<Mask> getMasks() {
    return Collections.unmodifiableList(masks);
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

    addEventConsumer(InventoryClickEvent.class, event -> {
      UUID uuid = event.getWhoClicked().getUniqueId();
      getDisplay(uuid).ifPresent(guiDisplay -> guiDisplay.handleClickEvent(uuid, event));
    });
  }

  @Override
  public void stop() {
    removeAllMasks().forEach(Mask::stop);
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
