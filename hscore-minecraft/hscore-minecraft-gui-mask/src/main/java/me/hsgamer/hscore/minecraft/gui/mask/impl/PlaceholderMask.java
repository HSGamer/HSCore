package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import me.hsgamer.hscore.minecraft.gui.mask.Mask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The placeholder mask <br> Used for per-user masks
 */
public class PlaceholderMask extends BaseMask {
  protected final Map<UUID, Mask> userMasks = new ConcurrentHashMap<>();
  protected final Mask defaultMask;

  /**
   * Create a new mask
   *
   * @param name        the name of the mask
   * @param defaultMask the default mask
   */
  public PlaceholderMask(@NotNull String name, @NotNull Mask defaultMask) {
    super(name);
    this.defaultMask = defaultMask;
  }

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  public PlaceholderMask(@NotNull String name) {
    this(name, Mask.empty(name + "_empty"));
  }

  @Override
  public @NotNull Map<Integer, Button> generateButtons(@NotNull UUID uuid) {
    return this.userMasks.getOrDefault(uuid, this.defaultMask).generateButtons(uuid);
  }

  @Override
  public void init() {
    this.defaultMask.init();
  }

  @Override
  public void stop() {
    this.defaultMask.stop();
    this.userMasks.clear();
  }

  /**
   * Set the mask for the unique id
   *
   * @param uuid the unique id
   * @param mask the mask
   */
  public void setMask(@NotNull UUID uuid, @Nullable Mask mask) {
    if (mask == null) {
      this.userMasks.remove(uuid);
    } else {
      this.userMasks.put(uuid, mask);
    }
  }

  /**
   * Get the mask for the unique id
   *
   * @param uuid the unique id
   *
   * @return the mask
   */
  @Nullable
  public Mask getMask(@NotNull UUID uuid) {
    return this.userMasks.get(uuid);
  }

  /**
   * Get the default mask
   *
   * @return the default mask
   */
  @NotNull
  public Mask getDefaultMask() {
    return defaultMask;
  }

  /**
   * Get the user-mask map
   *
   * @return the user-mask map
   */
  @NotNull
  public Map<@NotNull UUID, @NotNull Mask> getUserMasks() {
    return Collections.unmodifiableMap(this.userMasks);
  }
}
