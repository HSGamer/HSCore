package me.hsgamer.hscore.minecraft.gui.common.item;

import me.hsgamer.hscore.minecraft.gui.common.action.Action;
import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

/**
 * The action item
 */
public final class ActionItem {
  /**
   * An empty action item
   */
  public static final ActionItem EMPTY = new ActionItem();

  private @Nullable Object item;
  private @Nullable Action action;

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
  public @Nullable Action getAction() {
    return action;
  }

  /**
   * Set the action
   *
   * @param action the action
   *
   * @return this object
   */
  public ActionItem setAction(@Nullable Action action) {
    this.action = action;
    return this;
  }

  /**
   * Extend the action
   *
   * @param operator the operator
   *
   * @return this object
   */
  public ActionItem extendAction(UnaryOperator<@Nullable Action> operator) {
    this.action = operator.apply(this.action);
    return this;
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
