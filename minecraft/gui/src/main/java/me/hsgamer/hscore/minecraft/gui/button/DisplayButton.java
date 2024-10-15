package me.hsgamer.hscore.minecraft.gui.button;

import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.event.ViewerEvent;
import me.hsgamer.hscore.minecraft.gui.object.Item;
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

  private Item item;
  private Consumer<ViewerEvent> action;

  /**
   * Get the item to display
   *
   * @return the item
   */
  @Nullable
  public Item getItem() {
    return item;
  }

  /**
   * Set the item to display
   *
   * @param item the item
   *
   * @return the current instance
   */
  public DisplayButton setItem(@Nullable Item item) {
    this.item = item;
    return this;
  }

  /**
   * Get the action to handle the event
   *
   * @return the action
   */
  @Nullable
  public Consumer<ViewerEvent> getAction() {
    return action;
  }

  /**
   * Set the action to handle the event
   *
   * @param action the action
   *
   * @return the current instance
   */
  public DisplayButton setAction(@Nullable Consumer<ViewerEvent> action) {
    this.action = action;
    return this;
  }

  /**
   * Set the action to handle the click event
   *
   * @param action the action
   *
   * @return the current instance
   */
  public DisplayButton setClickAction(@Nullable Consumer<ClickEvent> action) {
    this.action = action == null ? null : event -> {
      if (event instanceof ClickEvent) {
        action.accept((ClickEvent) event);
      }
    };
    return this;
  }

  /**
   * Apply the display button to this instance
   *
   * @param displayButton the display button
   */
  public void apply(DisplayButton displayButton) {
    if (displayButton.item != null) {
      this.item = displayButton.item;
    }
    if (displayButton.action != null) {
      this.action = displayButton.action;
    }
  }
}
