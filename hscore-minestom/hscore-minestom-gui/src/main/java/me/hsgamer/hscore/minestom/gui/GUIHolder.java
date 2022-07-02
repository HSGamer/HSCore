package me.hsgamer.hscore.minestom.gui;

import me.hsgamer.hscore.minestom.gui.button.ButtonMap;
import me.hsgamer.hscore.ui.BaseHolder;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;

import java.util.Collections;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The base {@link me.hsgamer.hscore.ui.Holder} for UI in Minestom
 */
public class GUIHolder extends BaseHolder<GUIDisplay> {
  private boolean removeDisplayOnClose = true;
  private InventoryType inventoryType = InventoryType.CHEST_3_ROW;
  private Function<UUID, Component> titleFunction = uuid -> Component.empty();
  private Predicate<UUID> closeFilter = uuid -> true;
  private ButtonMap buttonMap = uuid -> Collections.emptyMap();

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
  public Function<UUID, Component> getTitleFunction() {
    return titleFunction;
  }

  /**
   * Set the title function
   *
   * @param titleFunction the title function
   */
  public void setTitleFunction(Function<UUID, Component> titleFunction) {
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
  public Component getTitle(UUID uuid) {
    return titleFunction.apply(uuid);
  }

  /**
   * Set the title
   *
   * @param title the title
   *
   * @see #setTitleFunction(Function)
   */
  public void setTitle(Component title) {
    setTitleFunction(uuid -> title);
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
      Player player = event.getPlayer();
      UUID uuid = player.getUuid();
      if (!closeFilter.test(uuid)) {
        event.setNewInventory(event.getInventory());
      } else if (removeDisplayOnClose) {
        removeDisplay(uuid);
      }
    });

    addEventConsumer(InventoryOpenEvent.class, this::onOpen);
    addEventConsumer(InventoryCloseEvent.class, this::onClose);
    addEventConsumer(InventoryClickEvent.class, this::onClick);
    addEventConsumer(InventoryPreClickEvent.class, this::onClick);

    addEventConsumer(InventoryClickEvent.class, event -> {
      UUID uuid = event.getPlayer().getUuid();
      getDisplay(uuid).ifPresent(guiDisplay -> guiDisplay.handleClick(uuid, event));
    });
    addEventConsumer(InventoryPreClickEvent.class, event -> {
      UUID uuid = event.getPlayer().getUuid();
      getDisplay(uuid).ifPresent(guiDisplay -> guiDisplay.handleClick(uuid, event));
    });

    buttonMap.init();
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
  protected void onClick(InventoryPreClickEvent event) {
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
