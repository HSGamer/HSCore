package me.hsgamer.hscore.minecraft.gui.common.item;

import me.hsgamer.hscore.minecraft.gui.common.action.Action;
import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

public final class ActionItem {
  public static final ActionItem EMPTY = new ActionItem();

  private @Nullable Object item;
  private @Nullable Action action;

  public @Nullable Object getItem() {
    return item;
  }

  public ActionItem setItem(@Nullable Object item) {
    this.item = item;
    return this;
  }

  public @Nullable Action getAction() {
    return action;
  }

  public ActionItem setAction(@Nullable Action action) {
    this.action = action;
    return this;
  }

  public ActionItem extendAction(UnaryOperator<@Nullable Action> operator) {
    this.action = operator.apply(this.action);
    return this;
  }

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
