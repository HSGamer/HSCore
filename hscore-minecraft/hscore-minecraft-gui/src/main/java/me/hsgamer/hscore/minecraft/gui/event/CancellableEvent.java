package me.hsgamer.hscore.minecraft.gui.event;

public interface CancellableEvent {
  boolean isCancelled();

  void setCancelled(final boolean cancelled);
}
