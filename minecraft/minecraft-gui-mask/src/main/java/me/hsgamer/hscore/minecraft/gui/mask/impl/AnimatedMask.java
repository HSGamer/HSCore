package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.GUIProperties;
import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import me.hsgamer.hscore.ui.property.IdentifiedUpdatable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The animated mask with child masks as frames
 */
public class AnimatedMask extends BaseMask implements IdentifiedUpdatable {
  private final List<Mask> masks = new ArrayList<>();
  private final Map<UUID, Integer> currentIndexMap = new ConcurrentHashMap<>();
  private final Map<UUID, Long> lastUpdateMap = new ConcurrentHashMap<>();
  private long periodMillis = 50;

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  public AnimatedMask(@NotNull String name) {
    super(name);
  }

  /**
   * Add mask(s)
   *
   * @param masks the mask (or frame)
   * @param <T>   the type of the mask
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public <T extends Mask> AnimatedMask addMask(@NotNull Collection<@NotNull T> masks) {
    this.masks.addAll(masks);
    return this;
  }

  /**
   * Add mask(s)
   *
   * @param mask the mask (or frame)
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public AnimatedMask addMask(@NotNull Mask... mask) {
    return addMask(Arrays.asList(mask));
  }

  /**
   * Set the period of the animation
   *
   * @param periodMillis the period in milliseconds
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public AnimatedMask setPeriodMillis(long periodMillis) {
    if (periodMillis <= 0) {
      throw new IllegalArgumentException("Period must be positive");
    }
    this.periodMillis = periodMillis;
    return this;
  }

  /**
   * Set the period of the animation
   *
   * @param periodTicks the period in ticks
   *
   * @return this instance
   */
  @Contract("_ -> this")
  public AnimatedMask setPeriodTicks(long periodTicks) {
    return setPeriodMillis(Math.max(periodTicks, 1) * GUIProperties.getMillisPerTick());
  }

  /**
   * Get the list of masks
   *
   * @return the masks
   */
  @NotNull
  public List<Mask> getMasks() {
    return masks;
  }

  private int getCurrentIndex(@NotNull UUID uuid) {
    return currentIndexMap.getOrDefault(uuid, 0);
  }

  @Override
  public Optional<Map<Integer, Button>> generateButtons(@NotNull UUID uuid, int size) {
    update(uuid);
    return masks.get(getCurrentIndex(uuid)).generateButtons(uuid, size);
  }

  @Override
  public void init() {
    if (this.masks.isEmpty()) {
      throw new IllegalArgumentException("There is no child mask for this animated mask");
    }
    this.masks.forEach(Mask::init);
  }

  @Override
  public void stop() {
    this.masks.forEach(Mask::stop);
  }

  @Override
  public void update(@NotNull UUID uuid) {
    long currentTimeMillis = System.currentTimeMillis();
    long lastUpdate = lastUpdateMap.computeIfAbsent(uuid, k -> currentTimeMillis);
    if (currentTimeMillis - lastUpdate < periodMillis) return;
    long diff = currentTimeMillis - lastUpdate;
    long remainder = diff % periodMillis;
    int skip = (int) (diff / periodMillis);
    int currentIndex = getCurrentIndex(uuid);
    currentIndexMap.put(uuid, (currentIndex + skip) % masks.size());
    lastUpdateMap.put(uuid, currentTimeMillis - remainder);
  }
}
