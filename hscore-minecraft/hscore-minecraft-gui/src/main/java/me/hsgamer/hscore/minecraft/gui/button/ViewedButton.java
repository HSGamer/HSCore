package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A button that is displayed on the inventory
 */
public class ViewedButton {
  /**
   * The empty button
   */
  public static final ViewedButton EMPTY = new ViewedButton();

  private Item displayItem;
  private Button button = Button.EMPTY;

  /**
   * Get the item to display
   *
   * @return the item
   */
  @Nullable
  public Item getDisplayItem() {
    return displayItem;
  }

  /**
   * Set the item to display
   *
   * @param displayItem the item
   */
  public void setDisplayItem(@Nullable Item displayItem) {
    this.displayItem = displayItem;
  }

  /**
   * Get the button
   *
   * @return the button
   */
  @NotNull
  public Button getButton() {
    return button;
  }

  /**
   * Set the button
   *
   * @param button the button
   */
  public void setButton(@NotNull Button button) {
    this.button = button;
  }

  /**
   * Handle the action
   *
   * @param event the event
   */
  public void handleAction(@NotNull ClickEvent event) {
    button.handleAction(event);
  }
}
