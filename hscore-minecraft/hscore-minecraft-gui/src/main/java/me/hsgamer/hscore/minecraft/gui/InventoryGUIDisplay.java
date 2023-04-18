package me.hsgamer.hscore.minecraft.gui;

import me.hsgamer.hscore.minecraft.gui.button.ViewedButton;
import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An implementation of {@link GUIDisplay} for Inventory-based GUI
 *
 * @param <H> the type of the holder
 */
public abstract class InventoryGUIDisplay<H extends GUIHolder<?>> extends GUIDisplay<H> {
  /**
   * The viewed buttons reference
   */
  private final AtomicReference<Map<Integer, ViewedButton>> viewedButtonsRef = new AtomicReference<>(Collections.emptyMap());

  /**
   * Create a new display
   *
   * @param uuid   the unique id
   * @param holder the holder
   */
  protected InventoryGUIDisplay(@NotNull UUID uuid, @NotNull H holder) {
    super(uuid, holder);
  }

  /**
   * Initialize the inventory
   */
  protected abstract void initInventory();

  /**
   * Clear the inventory
   */
  protected abstract void clearInventory();

  /**
   * Get the size of the inventory
   *
   * @return the size
   */
  protected abstract int getInventorySize();

  /**
   * Get the title of the inventory
   *
   * @param slot the slot
   * @param item the item
   */
  protected abstract void setButton(int slot, @Nullable Item item);

  @Override
  public void handleClickEvent(@NotNull ClickEvent event) {
    getViewedButton(event.getSlot()).ifPresent(button -> button.handleAction(event));
  }

  @Override
  public void init() {
    initInventory();
    update();
  }

  @Override
  public void stop() {
    clearInventory();
    viewedButtonsRef.set(Collections.emptyMap());
  }

  @Override
  public void update() {
    int size = getInventorySize();
    Map<Integer, ViewedButton> viewedButtons = holder.getButtonMap().getButtons(uuid, size);
    viewedButtonsRef.set(viewedButtons);
    for (int i = 0; i < size; i++) {
      setButton(i, viewedButtons.getOrDefault(i, ViewedButton.EMPTY).getDisplayItem());
    }
  }

  /**
   * Get the viewed button at the slot
   *
   * @param slot the slot
   *
   * @return the viewed button
   */
  public Optional<ViewedButton> getViewedButton(int slot) {
    return Optional.ofNullable(viewedButtonsRef.get()).map(viewedButtons -> viewedButtons.get(slot));
  }

  /**
   * Get the viewed buttons
   *
   * @return the viewed buttons
   */
  public Map<Integer, ViewedButton> getViewedButtons() {
    return Optional.ofNullable(viewedButtonsRef.get()).map(Collections::unmodifiableMap).orElse(Collections.emptyMap());
  }
}
