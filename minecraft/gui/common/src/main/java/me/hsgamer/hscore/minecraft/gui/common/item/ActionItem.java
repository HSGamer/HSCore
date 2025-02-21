package me.hsgamer.hscore.minecraft.gui.common.item;

import me.hsgamer.hscore.minecraft.gui.common.event.ViewerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The action item
 */
public final class ActionItem {
  private @Nullable Object item;
  private @Nullable Consumer<ViewerEvent> action;

  /**
   * Get the item
   *
   * @return the item
   */
  public @Nullable Object getItem() {
    return item;
  }

  /**
   * Set the item
   *
   * @param item the item
   *
   * @return this object
   */
  public ActionItem setItem(@Nullable Object item) {
    this.item = item;
    return this;
  }

  /**
   * Get the action
   *
   * @return the action
   */
  public @Nullable Consumer<ViewerEvent> getAction() {
    return action;
  }

  /**
   * Set the action
   *
   * @param action the action
   *
   * @return this object
   */
  public ActionItem setAction(@Nullable Consumer<ViewerEvent> action) {
    this.action = action;
    return this;
  }

  /**
   * Extend the action
   *
   * @param operator the operator with the event and the old action
   *
   * @return this object
   */
  public ActionItem extendAction(BiConsumer<ViewerEvent, Consumer<ViewerEvent>> operator) {
    Consumer<ViewerEvent> oldAction = this.action != null ? this.action : event -> {
    };
    this.action = event -> operator.accept(event, oldAction);
    return this;
  }

  /**
   * Call the action
   *
   * @param event the event
   */
  public void callAction(ViewerEvent event) {
    if (action != null) {
      action.accept(event);
    }
  }

  /**
   * Apply the action item
   *
   * @param actionItem the action item
   *
   * @return this object
   */
  public ActionItem apply(ActionItem actionItem) {
    if (actionItem.item != null) {
      this.item = actionItem.item;
    }
    if (actionItem.action != null) {
      this.action = actionItem.action;
    }
    return this;
  }
}
