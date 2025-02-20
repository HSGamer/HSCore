package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.minecraft.gui.common.button.Button;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The button with a list of child buttons
 */
public class ListButton implements Button {
  private final List<Button> buttons = new ArrayList<>();
  private final Map<UUID, Integer> currentIndexMap = new ConcurrentHashMap<>();
  private boolean keepCurrentIndex = false;

  /**
   * Add button(s)
   *
   * @param buttons the buttons
   * @param <T>     the type of the button
   */
  public <T extends Button> void addButton(@NotNull Collection<@NotNull T> buttons) {
    this.buttons.addAll(buttons);
  }

  /**
   * Add button(s)
   *
   * @param button the button
   */
  public void addButton(@NotNull Button... button) {
    addButton(Arrays.asList(button));
  }

  /**
   * Should the button keep the current index for the unique id on every {@link Button#getItem(InventoryContext)} times?
   *
   * @return true if it should
   */
  public boolean isKeepCurrentIndex() {
    return keepCurrentIndex;
  }

  /**
   * Should the button keep the current index for the unique id on every {@link Button#getItem(InventoryContext)} times?
   *
   * @param keepCurrentIndex true if it should
   */
  public void setKeepCurrentIndex(boolean keepCurrentIndex) {
    this.keepCurrentIndex = keepCurrentIndex;
  }

  /**
   * Remove the current index for the unique id
   *
   * @param uuid the unique id
   */
  public void removeCurrentIndex(UUID uuid) {
    this.currentIndexMap.remove(uuid);
  }

  /**
   * Get the list of buttons
   *
   * @return the buttons
   */
  public List<Button> getButtons() {
    return buttons;
  }

  @Override
  public void init() {
    this.buttons.forEach(Button::init);
  }

  @Override
  public void stop() {
    this.buttons.forEach(Button::stop);
  }

  @Override
  public @Nullable ActionItem getItem(@NotNull InventoryContext context) {
    UUID uuid = context.getViewerID();
    if (keepCurrentIndex && currentIndexMap.containsKey(uuid)) {
      return buttons.get(currentIndexMap.get(uuid)).getItem(context);
    }

    for (int i = 0; i < buttons.size(); i++) {
      Button button = buttons.get(i);
      ActionItem item = button.getItem(context);
      if (item != null) {
        currentIndexMap.put(uuid, i);
        return item;
      }
    }

    return null;
  }
}
