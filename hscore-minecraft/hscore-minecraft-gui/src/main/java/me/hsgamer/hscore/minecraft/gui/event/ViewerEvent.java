package me.hsgamer.hscore.minecraft.gui.event;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface ViewerEvent {
  @NotNull
  UUID getViewerID();
}
