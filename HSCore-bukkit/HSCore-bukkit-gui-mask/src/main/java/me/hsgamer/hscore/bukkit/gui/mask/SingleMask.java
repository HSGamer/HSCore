package me.hsgamer.hscore.bukkit.gui.mask;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.common.Pair;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple mask with a single {@link Button}
 */
public class SingleMask extends BaseMask implements MultiUserMask {
  private final int slot;
  private final Map<UUID, Button> userButtons = new ConcurrentHashMap<>();
  private final Button defaultButton;

  /**
   * Create a new mask
   *
   * @param name          the name of the mask
   * @param slot          the slot
   * @param defaultButton the default button
   */
  public SingleMask(String name, int slot, Button defaultButton) {
    super(name);
    this.slot = slot;
    this.defaultButton = defaultButton;
  }

  /**
   * Create a new mask
   *
   * @param name          the name of the mask
   * @param position      the position
   * @param defaultButton the default button
   */
  public SingleMask(String name, Pair<Integer, Integer> position, Button defaultButton) {
    this(name, MaskUtils.toSlot(position), defaultButton);
  }

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    return Collections.singletonMap(slot, this.getButton(uuid));
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
    return this.userButtons.getOrDefault(uuid, defaultButton);
  }
}
