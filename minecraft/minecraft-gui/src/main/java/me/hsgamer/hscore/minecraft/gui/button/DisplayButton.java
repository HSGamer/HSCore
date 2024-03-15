package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * A button that is displayed on the inventory
 */
public class DisplayButton {
  /**
   * The empty button
   */
  public static final DisplayButton EMPTY = new DisplayButton();

  private Item displayItem;
  private Button button = Button.EMPTY;
  private Consumer<ClickEvent> action = null;

  /**
   * Create a new instance
   */
  public DisplayButton() {
  }

  /**
   * Create a new instance
   *
   * @param displayItem the item to display
   * @param button      the button
   * @param action      the action
   */
  public DisplayButton(@Nullable Item displayItem, @NotNull Button button, @Nullable Consumer<ClickEvent> action) {
    this.displayItem = displayItem;
    this.button = button;
    this.action = action;
  }

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
   * Get the action
   *
   * @return the action
   */
  @Nullable
  public Consumer<ClickEvent> getAction() {
    return action;
  }

  /**
   * Set the action
   *
   * @param action the action
   */
  public void setAction(@Nullable Consumer<ClickEvent> action) {
    this.action = action;
  }
}
