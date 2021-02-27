package me.hsgamer.hscore.bukkit.gui.mask.multi;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.bukkit.gui.mask.simple.MultiSlotsMask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MultiUserMultiSlotsMask extends MultiSlotsMask implements PerUserSetButtons, PerUserGetButtons {
  protected final Map<UUID, Button> userButtons = new ConcurrentHashMap<>();
  private boolean stopUserButtons = false;

  public MultiUserMultiSlotsMask(String name, List<Integer> slots, Button button) {
    super(name, slots, button);
  }

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    Button finalButton = this.userButtons.getOrDefault(uuid, this.button);
    Map<Integer, Button> map = new HashMap<>();
    this.slots.forEach(slot -> map.put(slot, finalButton));
    return map;
  }

  @Override
  public void stop() {
    super.stop();
    if (stopUserButtons) {
      this.userButtons.values().forEach(Button::stop);
    }
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

  /**
   * Will this mask also stop the user buttons on stop
   *
   * @param stopUserButtons true if it will
   */
  public void setStopUserButtons(boolean stopUserButtons) {
    this.stopUserButtons = stopUserButtons;
  }
}
