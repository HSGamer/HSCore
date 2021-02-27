package me.hsgamer.hscore.bukkit.gui.mask.simple;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.bukkit.gui.mask.BaseMask;
import me.hsgamer.hscore.bukkit.gui.mask.MaskUtils;
import me.hsgamer.hscore.common.Pair;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * The simple mask with a single {@link Button}
 */
public class SingleMask extends BaseMask {
  protected final int slot;
  protected final Button button;

  /**
   * Create a new mask
   *
   * @param name   the name of the mask
   * @param slot   the slot
   * @param button the button
   */
  public SingleMask(String name, int slot, Button button) {
    super(name);
    this.slot = slot;
    this.button = button;
  }

  /**
   * Create a new mask
   *
   * @param name     the name of the mask
   * @param position the position
   * @param button   the button
   */
  public SingleMask(String name, Pair<Integer, Integer> position, Button button) {
    this(name, MaskUtils.toSlot(position), button);
  }

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    return Collections.singletonMap(this.slot, this.button);
  }

  @Override
  public void init() {
    this.button.init();
  }

  @Override
  public void stop() {
    this.button.stop();
  }
}
