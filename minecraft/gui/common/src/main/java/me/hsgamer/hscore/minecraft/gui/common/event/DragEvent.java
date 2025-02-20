package me.hsgamer.hscore.minecraft.gui.common.event;

public interface DragEvent extends ViewerEvent, CancellableEvent {
  /**
   * Get the slots that are being dragged
   *
   * @return the slots
   */
  int[] getSlots();
}
