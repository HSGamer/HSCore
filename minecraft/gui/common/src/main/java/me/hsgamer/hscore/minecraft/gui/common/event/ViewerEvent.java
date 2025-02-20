package me.hsgamer.hscore.minecraft.gui.common.event;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The event that has a viewer
 */
public interface ViewerEvent {
  /**
   * Get the viewer
   *
   * @return the viewer
   */
  @NotNull
  UUID getViewerID();
}
