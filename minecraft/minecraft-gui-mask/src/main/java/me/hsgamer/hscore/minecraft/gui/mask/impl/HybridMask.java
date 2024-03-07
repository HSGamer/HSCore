package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The mask that views multiple masks
 */
public class HybridMask extends BaseMask {
  private final List<Mask> maskList = new ArrayList<>();

  /**
   * Create a new hybrid mask
   *
   * @param name the name
   */
  public HybridMask(@NotNull String name) {
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
  public <T extends Mask> HybridMask addMask(@NotNull Collection<T> masks) {
    maskList.addAll(masks);
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
  public HybridMask addMask(@NotNull Mask... mask) {
    return addMask(Arrays.asList(mask));
  }

  /**
   * Get the masks
   *
   * @return the masks
   */
  public Collection<Mask> getMasks() {
    return Collections.unmodifiableList(maskList);
  }

  @Override
  public Optional<Map<Integer, Button>> generateButtons(@NotNull UUID uuid, int size) {
    Map<Integer, Button> buttonMap = new HashMap<>();
    for (Mask mask : maskList) {
      mask.generateButtons(uuid, size).ifPresent(buttonMap::putAll);
    }
    return Optional.of(buttonMap);
  }

  @Override
  public void init() {
    maskList.forEach(Mask::init);
  }

  @Override
  public void stop() {
    maskList.forEach(Mask::stop);
  }
}
