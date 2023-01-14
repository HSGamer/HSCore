package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.bukkit.gui.mask.BaseMask;
import me.hsgamer.hscore.bukkit.gui.mask.Mask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The mask with a list of child masks
 */
public class ListMask extends BaseMask {
  private final List<Mask> masks = new ArrayList<>();
  private final Map<UUID, Integer> currentIndexMap = new ConcurrentHashMap<>();

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  public ListMask(String name, Mask... childMask) {
    super(name);
    addChildMasks(childMask);
  }

  /**
   * Add child masks
   *
   * @param childMask the child mask (or frame)
   */
  public void addChildMasks(Mask... childMask) {
    this.masks.addAll(Arrays.asList(childMask));
  }

  @Override
  public boolean canView(UUID uuid) {
    for (int i = 0; i < masks.size(); i++) {
      Mask mask = masks.get(i);
      if (mask.canView(uuid)) {
        currentIndexMap.put(uuid, i);
        return true;
      }
    }
    return false;
  }

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    return Optional.ofNullable(currentIndexMap.get(uuid))
      .map(masks::get)
      .map(mask -> mask.generateButtons(uuid))
      .orElseGet(Collections::emptyMap);
  }

  @Override
  public void init() {
    masks.forEach(Mask::init);
  }

  @Override
  public void stop() {
    masks.forEach(Mask::stop);
  }

  /**
   * Get the list of masks
   *
   * @return the masks
   */
  public List<Mask> getMasks() {
    return masks;
  }
}
