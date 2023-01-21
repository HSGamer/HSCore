package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
  public ListMask(@NotNull String name) {
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
  public <T extends Mask> ListMask addMask(@NotNull Collection<@NotNull T> masks) {
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
  public ListMask addMask(@NotNull Mask... mask) {
    return addMask(Arrays.asList(mask));
  }

  /**
   * Get the list of masks
   *
   * @return the masks
   */
  @NotNull
  public List<@NotNull Mask> getMasks() {
    return masks;
  }

  @Override
  public boolean canView(@NotNull UUID uuid) {
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
  public @NotNull Map<Integer, Button> generateButtons(@NotNull UUID uuid) {
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
}
