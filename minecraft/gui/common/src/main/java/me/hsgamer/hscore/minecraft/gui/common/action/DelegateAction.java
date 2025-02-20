package me.hsgamer.hscore.minecraft.gui.common.action;

import me.hsgamer.hscore.minecraft.gui.common.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.common.event.DragEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The delegate action. Used to override / extend the action
 */
public class DelegateAction implements Action {
  private final @NotNull Action delegate;

  /**
   * Create a new delegate action
   *
   * @param delegate the delegate action
   */
  public DelegateAction(@NotNull Action delegate) {
    this.delegate = delegate;
  }

  @Override
  public void handleClick(ClickEvent event) {
    delegate.handleClick(event);
  }

  @Override
  public void handleDrag(DragEvent event) {
    delegate.handleDrag(event);
  }
}
