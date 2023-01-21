package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * The mask that views multiple masks
 */
public class HybridMask extends BaseMask {
  private final Map<Mask, Set<UUID>> maskMap = new LinkedHashMap<>();

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
    masks.forEach(mask -> maskMap.put(mask, new ConcurrentSkipListSet<>()));
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
    return Collections.unmodifiableCollection(maskMap.keySet());
  }

  @Override
  public @NotNull Map<Integer, Button> generateButtons(@NotNull UUID uuid) {
    Map<Integer, Button> buttonMap = new HashMap<>();
    for (Map.Entry<Mask, Set<UUID>> entry : maskMap.entrySet()) {
      if (entry.getValue().contains(uuid)) {
        buttonMap.putAll(entry.getKey().generateButtons(uuid));
      }
    }
    return buttonMap;
  }

  @Override
  public boolean canView(@NotNull UUID uuid) {
    boolean canView = false;
    for (Map.Entry<Mask, Set<UUID>> entry : maskMap.entrySet()) {
      Set<UUID> uuidSet = entry.getValue();
      if (entry.getKey().canView(uuid)) {
        uuidSet.add(uuid);
        canView = true;
      } else {
        uuidSet.remove(uuid);
      }
    }
    return canView;
  }

  @Override
  public void init() {
    maskMap.keySet().forEach(Mask::init);
  }

  @Override
  public void stop() {
    maskMap.keySet().forEach(Mask::stop);
    maskMap.clear();
  }
}
