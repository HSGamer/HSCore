package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.ui.BaseHolder;

import java.util.UUID;

// TODO: Add items, actions and event handling
public class GUIHolder extends BaseHolder<GUIDisplay> {

  @Override
  protected GUIDisplay newDisplay(UUID uuid) {
    return new GUIDisplay(uuid, this);
  }
}
