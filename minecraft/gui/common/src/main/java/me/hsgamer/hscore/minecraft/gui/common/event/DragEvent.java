package me.hsgamer.hscore.minecraft.gui.common.event;

import java.util.Collection;

/**
 * The event when a player drags on the GUI
 */
public interface DragEvent extends ViewerEvent, CancellableEvent {
  /**
   * Get the slots that are being dragged
   *
   * @return the slots
   */
  Collection<Integer> getSlots();
}
