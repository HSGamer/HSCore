package me.hsgamer.hscore.minecraft.gui.common.event;

import java.util.Collection;

public interface DragEvent extends ViewerEvent, CancellableEvent {
  /**
   * Get the slots that are being dragged
   *
   * @return the slots
   */
  Collection<Integer> getSlots();
}
