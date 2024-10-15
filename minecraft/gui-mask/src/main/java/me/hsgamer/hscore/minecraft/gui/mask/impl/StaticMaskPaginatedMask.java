package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The {@link MaskPaginatedMask} with the static list of masks
 */
public class StaticMaskPaginatedMask extends MaskPaginatedMask {
  protected final List<Mask> masks = new ArrayList<>();

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  public StaticMaskPaginatedMask(@NotNull String name) {
    super(name);
  }

  /**
   * Add mask(s)
   *
   * @param masks the mask
   * @param <T>   the type of the mask
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public <T extends Mask> StaticMaskPaginatedMask addMask(@NotNull Collection<@NotNull T> masks) {
    this.masks.addAll(masks);
    return this;
  }

  /**
   * Add mask(s)
   *
   * @param mask the mask
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public StaticMaskPaginatedMask addMask(@NotNull Mask... mask) {
    return addMask(Arrays.asList(mask));
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
