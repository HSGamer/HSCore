package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.GUIProperties;
import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import me.hsgamer.hscore.ui.property.IdentifiedUpdatable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The animated mask with child masks as frames, but only run once
 */
public class OneTimeAnimatedMask extends BaseMask implements IdentifiedUpdatable {
  private final List<Mask> masks = new ArrayList<>();
  private final Map<UUID, SequenceRunner> runnerMap = new ConcurrentHashMap<>();
  private boolean viewLast = false;
  private long periodMillis = 50L;

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  public OneTimeAnimatedMask(@NotNull String name) {
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
  public <T extends Mask> OneTimeAnimatedMask addMask(@NotNull Collection<@NotNull T> masks) {
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
  public OneTimeAnimatedMask addMask(@NotNull Mask... mask) {
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
  public OneTimeAnimatedMask setPeriodMillis(long periodMillis) {
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
  public OneTimeAnimatedMask setPeriodTicks(long periodTicks) {
    return setPeriodMillis(Math.max(periodTicks, 1) * GUIProperties.getMillisPerTick());
  }

  /**
   * Set whether to view the last frame when the animation is finished
   *
   * @param viewLast true to view the last frame
   */
  @Contract("_ -> this")
  public OneTimeAnimatedMask setViewLast(boolean viewLast) {
    this.viewLast = viewLast;
    return this;
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

  /**
   * Reset the animation for the unique id
   *
   * @param uuid the unique id
   */
  public void reset(@NotNull UUID uuid) {
    getRunner(uuid).reset();
  }

  private SequenceRunner getRunner(@NotNull UUID uuid) {
    return runnerMap.computeIfAbsent(uuid, k -> new SequenceRunner());
  }

  @Override
  public Optional<Map<@NotNull Integer, @NotNull Button>> generateButtons(@NotNull UUID uuid, @NotNull InventorySize inventorySize) {
    update(uuid);
    SequenceRunner runner = getRunner(uuid);
    if (runner.maxed && !viewLast) {
      return Optional.empty();
    }
    return masks.get(runner.index).generateButtons(uuid, inventorySize);
  }

  @Override
  public void update(@NotNull UUID uuid) {
    SequenceRunner runner = getRunner(uuid);
    if (!runner.maxed) {
      runner.updateIndex();
    }
  }

  @Override
  public void init() {
    if (this.masks.isEmpty()) {
      throw new IllegalArgumentException("There is no child mask for this one-time animated mask");
    }
    this.masks.forEach(Mask::init);
  }

  @Override
  public void stop() {
    runnerMap.clear();
    masks.forEach(Mask::stop);
    masks.clear();
  }

  private class SequenceRunner {
    private int index = 0;
    private long lastTickMillis = System.currentTimeMillis();
    private boolean maxed = false;

    private void updateIndex() {
      long currentTick = System.currentTimeMillis();
      long diff = currentTick - lastTickMillis;
      if (diff < periodMillis) {
        return;
      }

      int passed = (int) (diff / periodMillis);
      long remainder = diff % periodMillis;
      lastTickMillis = currentTick - remainder;

      int max = masks.size();
      if (index + passed >= max) {
        index = max - 1;
        maxed = true;
      } else {
        index += passed;
      }
    }

    private void reset() {
      index = 0;
      lastTickMillis = System.currentTimeMillis();
      maxed = false;
    }
  }
}
