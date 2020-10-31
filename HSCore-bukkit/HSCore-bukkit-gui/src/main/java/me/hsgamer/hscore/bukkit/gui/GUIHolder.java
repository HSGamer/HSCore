package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.ui.BaseHolder;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

// TODO: Add items, actions and event handling
public class GUIHolder extends BaseHolder<GUIDisplay> {
  private final HashMap<UUID, GUIDisplay> displayMap = new HashMap<>();

  @Override
  public GUIDisplay createDisplay(UUID uuid) {
    GUIDisplay display = new GUIDisplay(uuid, this);
    displayMap.put(uuid, display);
    return display;
  }

  @Override
  public void removeDisplay(UUID uuid) {
    Optional.ofNullable(displayMap.remove(uuid)).ifPresent(GUIDisplay::close);
  }

  @Override
  public Optional<GUIDisplay> getDisplay(UUID uuid) {
    return Optional.ofNullable(displayMap.get(uuid));
  }

  @Override
  public void updateAll() {
    displayMap.values().forEach(GUIDisplay::update);
  }
}
