package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.mask.Mask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The {@link MaskPaginatedMask} with the static list of masks
 */
public class StaticMaskPaginatedMask extends MaskPaginatedMask {
  protected final List<Mask> masks = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param name  the name of the mask
   * @param masks the masks
   */
  public StaticMaskPaginatedMask(String name, Mask... masks) {
    super(name);
    this.addMasks(masks);
  }

  /**
   * Create a new mask
   *
   * @param name  the name of the mask
   * @param masks the masks
   */
  public StaticMaskPaginatedMask(String name, List<Mask> masks) {
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

  @Override
  public List<Mask> getMasks(UUID uuid) {
    return this.masks;
  }

  @Override
  public void init() {
    this.masks.forEach(Mask::init);
  }

  @Override
  public void stop() {
    this.masks.forEach(Mask::stop);
    this.masks.clear();
    super.stop();
  }
}
