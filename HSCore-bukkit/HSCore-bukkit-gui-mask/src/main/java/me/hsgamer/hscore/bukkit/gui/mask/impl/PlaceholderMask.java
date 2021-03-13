package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.bukkit.gui.mask.BaseMask;
import me.hsgamer.hscore.bukkit.gui.mask.Mask;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The placeholder mask <br> Used for per-user masks
 */
public class PlaceholderMask extends BaseMask {
  protected final Map<UUID, Mask> userMasks = new ConcurrentHashMap<>();
  protected final Mask defaultMask;

  /**
   * Create a new mask
   *
   * @param name        the name of the mask
   * @param defaultMask the default mask
   */
  public PlaceholderMask(String name, Mask defaultMask) {
    super(name);
    this.defaultMask = defaultMask;
  }

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    return this.userMasks.getOrDefault(uuid, this.defaultMask).generateButtons(uuid);
  }

  @Override
  public void init() {
    this.defaultMask.init();
  }

  @Override
  public void stop() {
    this.defaultMask.stop();
    this.userMasks.clear();
  }

  /**
   * Set the mask for the unique id
   *
   * @param uuid the unique id
   * @param mask the mask
   */
  public void setMask(UUID uuid, Mask mask) {
    this.userMasks.put(uuid, mask);
  }

  /**
   * Get the mask for the unique id
   *
   * @param uuid the unique id
   *
   * @return the mask
   */
  public Mask getMask(UUID uuid) {
    return this.userMasks.get(uuid);
  }

  /**
   * Get the user-mask map
   *
   * @return the user-mask map
   */
  public Map<UUID, Mask> getUserMasks() {
    return Collections.unmodifiableMap(this.userMasks);
  }
}
