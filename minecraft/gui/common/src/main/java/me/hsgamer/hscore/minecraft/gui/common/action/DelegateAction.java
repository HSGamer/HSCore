package me.hsgamer.hscore.minecraft.gui.common.action;

import me.hsgamer.hscore.minecraft.gui.common.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.common.event.DragEvent;
import org.jetbrains.annotations.NotNull;

public class DelegateAction implements Action {
  private final @NotNull Action delegate;

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
