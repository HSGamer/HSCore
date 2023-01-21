package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import me.hsgamer.hscore.ui.property.IdentifiedUpdatable;
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
  private final long periodMillis;

  /**
   * Create a new mask
   *
   * @param name         the name of the mask
   * @param periodMillis the delay between each frame
   * @param childMask    the child mask (or frame)
   */
  public AnimatedMask(@NotNull String name, long periodMillis, @NotNull Mask... childMask) {
    super(name);
    if (periodMillis <= 0) {
      throw new IllegalArgumentException("Period must be positive");
    }
    this.periodMillis = periodMillis;
    addChildMasks(childMask);
  }

  /**
   * Add child masks
   *
   * @param childMask the child mask (or frame)
   */
  public void addChildMasks(@NotNull Mask... childMask) {
    this.masks.addAll(Arrays.asList(childMask));
  }

  private int getCurrentIndex(@NotNull UUID uuid) {
    return currentIndexMap.getOrDefault(uuid, 0);
  }

  @Override
  public @NotNull Map<Integer, Button> generateButtons(@NotNull UUID uuid) {
    return masks.get(getCurrentIndex(uuid)).generateButtons(uuid);
  }

  @Override
  public boolean canView(@NotNull UUID uuid) {
    update(uuid);
    return masks.get(getCurrentIndex(uuid)).canView(uuid);
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
  public void update(UUID uuid) {
    long currentTimeMillis = System.currentTimeMillis();
    long lastUpdate = lastUpdateMap.computeIfAbsent(uuid, k -> currentTimeMillis);
    if (currentTimeMillis - lastUpdate < periodMillis) return;
    int skip = (int) Math.floor((currentTimeMillis - lastUpdate) / (double) periodMillis);
    int currentIndex = getCurrentIndex(uuid);
    currentIndexMap.put(uuid, (currentIndex + skip) % masks.size());
    lastUpdateMap.put(uuid, currentTimeMillis);
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
}
