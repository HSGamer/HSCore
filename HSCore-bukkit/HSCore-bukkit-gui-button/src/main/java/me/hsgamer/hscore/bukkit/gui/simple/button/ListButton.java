package me.hsgamer.hscore.bukkit.gui.simple.button;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * The button with a list of child buttons
 */
public class ListButton implements Button {
  private final List<Button> buttons = new ArrayList<>();
  private final Map<UUID, Integer> currentIndexMap = new HashMap<>();
  private boolean keepCurrentIndex = false;

  /**
   * Create a new button
   *
   * @param buttons the child buttons
   */
  public ListButton(List<Button> buttons) {
    this.buttons.addAll(buttons);
  }

  /**
   * Create a new button
   *
   * @param buttons the child buttons
   */
  public ListButton(Button... buttons) {
    this(Arrays.asList(buttons));
  }

  @Override
  public ItemStack getItemStack(UUID uuid) {
    if (keepCurrentIndex && currentIndexMap.containsKey(uuid)) {
      return buttons.get(currentIndexMap.get(uuid)).getItemStack(uuid);
    }

    for (int i = 0; i < buttons.size(); i++) {
      Button button = buttons.get(i);
      ItemStack item = button.getItemStack(uuid);
      if (item != null) {
        currentIndexMap.put(uuid, i);
        return item;
      }
    }

    return null;
  }

  @Override
  public void handleAction(UUID uuid, InventoryClickEvent event) {
    Optional.ofNullable(currentIndexMap.get(uuid))
      .map(buttons::get)
      .ifPresent(button -> button.handleAction(uuid, event));
  }

  @Override
  public void init() {
    // EMPTY
  }

  @Override
  public void stop() {
    // EMPTY
  }

  /**
   * Should the button keep the current index for the unique id on every {@link #getItemStack(UUID)} times?
   *
   * @param keepCurrentIndex true if it should
   */
  public void setKeepCurrentIndex(boolean keepCurrentIndex) {
    this.keepCurrentIndex = keepCurrentIndex;
  }

  /**
   * Get the list of buttons
   *
   * @return the buttons
   */
  public List<Button> getButtons() {
    return buttons;
  }
}
