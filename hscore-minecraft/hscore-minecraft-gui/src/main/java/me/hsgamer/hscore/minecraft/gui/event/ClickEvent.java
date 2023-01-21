package me.hsgamer.hscore.minecraft.gui.event;

public interface ClickEvent extends ViewerEvent, CancellableEvent {
  int getSlot();
}
