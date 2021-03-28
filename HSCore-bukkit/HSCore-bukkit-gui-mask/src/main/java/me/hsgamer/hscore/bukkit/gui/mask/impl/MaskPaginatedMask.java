package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.bukkit.gui.mask.Mask;

import java.util.*;

/**
 * The mask paginated mask, those with a long list of {@link Mask} divided into pages.
 */
public class MaskPaginatedMask extends PaginatedMask {
  protected final List<Mask> masks = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param name  the name of the mask
   * @param masks the masks
   */
  public MaskPaginatedMask(String name, Mask... masks) {
    super(name);
    this.addMasks(masks);
  }

  /**
   * Add masks to the masks
   *
   * @param masks the masks
   */
  public void addMasks(Mask... masks) {
    this.addMasks(Arrays.asList(masks));
  }

  /**
   * Add masks to the masks
   *
   * @param masks the masks
   */
  public void addMasks(List<Mask> masks) {
    this.masks.addAll(masks);
  }

  /**
   * Get the masks
   *
   * @return the masks
   */
  public List<Mask> getMasks() {
    return Collections.unmodifiableList(masks);
  }

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    return this.masks.isEmpty() ? Collections.emptyMap() : this.masks.get(this.getPage(uuid)).generateButtons(uuid);
  }

  @Override
  protected int getPageAmount() {
    return this.masks.size();
  }

  @Override
  public void init() {
    this.masks.forEach(Mask::init);
  }

  @Override
  public void stop() {
    this.masks.forEach(Mask::stop);
    this.pageNumberMap.clear();
  }
}
