package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import org.jetbrains.annotations.NotNull;

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
  public StaticMaskPaginatedMask(@NotNull String name, @NotNull Mask... masks) {
    super(name);
    this.addMasks(masks);
  }

  /**
   * Create a new mask
   *
   * @param name  the name of the mask
   * @param masks the masks
   */
  public StaticMaskPaginatedMask(@NotNull String name, @NotNull List<@NotNull Mask> masks) {
    super(name);
    this.addMasks(masks);
  }

  /**
   * Add masks to the masks
   *
   * @param masks the masks
   */
  public void addMasks(@NotNull Mask... masks) {
    this.addMasks(Arrays.asList(masks));
  }

  /**
   * Add masks to the masks
   *
   * @param masks the masks
   */
  public void addMasks(@NotNull List<@NotNull Mask> masks) {
    this.masks.addAll(masks);
  }

  @Override
  public @NotNull List<Mask> getMasks(@NotNull UUID uuid) {
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
