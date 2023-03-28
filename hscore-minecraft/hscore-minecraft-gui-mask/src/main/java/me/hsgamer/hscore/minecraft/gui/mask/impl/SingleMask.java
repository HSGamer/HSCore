package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * The simple mask with a single {@link Button}
 */
public class SingleMask extends BaseMask {
  protected final int slot;
  protected final Button button;

  /**
   * Create a new mask
   *
   * @param name   the name of the mask
   * @param slot   the slot
   * @param button the button
   */
  public SingleMask(@NotNull String name, int slot, @NotNull Button button) {
    super(name);
    this.slot = slot;
    this.button = button;
  }

  @Override
  public @NotNull Map<Integer, Button> generateButtons(@NotNull UUID uuid, int size) {
    return Collections.singletonMap(this.slot, this.button);
  }

  @Override
  public void init() {
    this.button.init();
  }

  @Override
  public void stop() {
    this.button.stop();
  }
}
