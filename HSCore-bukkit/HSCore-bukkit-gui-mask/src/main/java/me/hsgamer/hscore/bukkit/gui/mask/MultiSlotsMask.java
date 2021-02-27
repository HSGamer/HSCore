package me.hsgamer.hscore.bukkit.gui.mask;

import me.hsgamer.hscore.bukkit.gui.button.Button;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The masks with multiple slots
 */
public class MultiSlotsMask extends BaseMask implements MultiUserMask {
  private final List<Integer> slots = new ArrayList<>();
  private final Map<UUID, Button> userButtons = new ConcurrentHashMap<>();
  private final Button defaultButton;

  /**
   * Create a new mask
   *
   * @param name          the name of the mask
   * @param slots         the slots
   * @param defaultButton the default button
   */
  public MultiSlotsMask(String name, List<Integer> slots, Button defaultButton) {
    super(name);
    this.slots.addAll(slots);
    this.defaultButton = defaultButton;
  }

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    Button button = this.getButton(uuid);
    Map<Integer, Button> map = new HashMap<>();
    this.slots.forEach(slot -> map.put(slot, button));
    return map;
  }

  @Override
  public void init() {
    this.defaultButton.init();
  }

  @Override
  public void stop() {
    this.defaultButton.stop();
    this.userButtons.clear();
  }

  @Override
  public void setButton(UUID uuid, Button button) {
    this.userButtons.put(uuid, button);
  }

  @Override
  public Button getButton(UUID uuid) {
    return this.userButtons.getOrDefault(uuid, this.defaultButton);
  }
}
