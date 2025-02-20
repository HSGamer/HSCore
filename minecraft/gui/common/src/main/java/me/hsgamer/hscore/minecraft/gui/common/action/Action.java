package me.hsgamer.hscore.minecraft.gui.common.action;

import me.hsgamer.hscore.minecraft.gui.common.event.ClickEvent;
import me.hsgamer.hscore.minecraft.gui.common.event.DragEvent;

public interface Action {
  Action EMPTY = new Action() {
  };

  default void handleClick(ClickEvent event) {

  }

  default void handleDrag(DragEvent event) {

  }
}
