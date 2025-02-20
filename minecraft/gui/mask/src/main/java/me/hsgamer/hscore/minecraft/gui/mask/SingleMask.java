package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.button.Button;
import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

/**
 * The simple mask with a single {@link Button}
 */
public class SingleMask implements ButtonMap {
  protected final int slot;
  protected final Button button;

  /**
   * Create a new mask
   *
   * @param slot   the slot
   * @param button the button
   */
  public SingleMask(int slot, @NotNull Button button) {
    this.slot = slot;
    this.button = button;
  }

  @Override
  public void init() {
    this.button.init();
  }

  @Override
  public void stop() {
    this.button.stop();
  }

  @Override
  public @Nullable Map<Integer, ActionItem> getItemMap(@NotNull InventoryContext context) {
    ActionItem actionItem = this.button.getItem(context);
    return actionItem == null ? null : Collections.singletonMap(this.slot, actionItem);
  }
}
