package me.hsgamer.hscore.minecraft.gui.object;

import me.hsgamer.hscore.minecraft.gui.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.event.ViewerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * An item with an action
 */
public class ActionItem {
  /**
   * The empty action item
   */
  public static final ActionItem EMPTY = new ActionItem();

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
  public ActionItem setItem(@Nullable Item item) {
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
  public ActionItem setAction(@Nullable Consumer<ViewerEvent> action) {
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
  public ActionItem setClickAction(@Nullable Consumer<ClickEvent> action) {
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
   * @param actionItem the display button
   */
  public void apply(ActionItem actionItem) {
    if (actionItem.item != null) {
      this.item = actionItem.item;
    }
    if (actionItem.action != null) {
      this.action = actionItem.action;
    }
  }
}
