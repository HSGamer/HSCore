package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.bukkit.gui.mask.PerUserGetButtons;
import me.hsgamer.hscore.bukkit.gui.mask.PerUserSetButtons;
import me.hsgamer.hscore.ui.Position2D;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MultiUserSingleMask extends SingleMask implements PerUserSetButtons, PerUserGetButtons {
  protected final Map<UUID, Button> userButtons = new ConcurrentHashMap<>();

  public MultiUserSingleMask(String name, int slot, Button button) {
    super(name, slot, button);
  }

  public MultiUserSingleMask(String name, Position2D position, Button button) {
    super(name, position, button);
  }

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    return Collections.singletonMap(this.slot, this.userButtons.getOrDefault(uuid, this.button));
  }

  @Override
  public void stop() {
    super.stop();
    this.userButtons.clear();
  }

  @Override
  public Map<UUID, List<Button>> getUserButtons() {
    Map<UUID, List<Button>> map = new HashMap<>();
    this.userButtons.forEach((uuid, button1) -> map.put(uuid, Collections.singletonList(button1)));
    return map;
  }

  @Override
  public void setButtons(UUID uuid, List<Button> buttons) {
    this.userButtons.put(uuid, Optional.of(buttons)
      .filter(buttons1 -> !buttons1.isEmpty())
      .map(buttons1 -> buttons1.get(0))
      .orElse(null)
    );
  }
}
