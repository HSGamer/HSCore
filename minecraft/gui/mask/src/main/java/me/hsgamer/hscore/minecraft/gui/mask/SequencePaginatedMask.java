package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.button.Button;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import me.hsgamer.hscore.minecraft.gui.mask.util.MaskSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A button paginated mask, where each {@link Button} is a page
 */
public abstract class SequencePaginatedMask extends PaginatedMask {
  protected final MaskSlot maskSlot;

  /**
   * Create a new mask
   *
   * @param maskSlot the mask slot
   */
  protected SequencePaginatedMask(@NotNull MaskSlot maskSlot) {
    this.maskSlot = maskSlot;
  }

  /**
   * Get the mask slot
   *
   * @return the mask slot
   */
  @NotNull
  public MaskSlot getMaskSlot() {
    return this.maskSlot;
  }

  /**
   * Get the buttons for the unique id
   *
   * @param uuid the unique id
   *
   * @return the buttons
   */
  @NotNull
  public abstract List<@NotNull Button> getButtons(UUID uuid);

  @Override
  protected @Nullable Map<@NotNull Integer, @NotNull ActionItem> getItemMap(@NotNull InventoryContext context, int pageNumber) {
    List<Integer> slots = this.maskSlot.apply(context);
    List<Button> buttons = getButtons(context.getViewerID());
    if (buttons.isEmpty() || slots.isEmpty()) {
      return null;
    }

    int pageAmount = buttons.size();
    pageAmount = this.getAndSetExactPage(context.getViewerID(), pageNumber, pageAmount);

    Map<Integer, ActionItem> map = new HashMap<>();
    int basePage = this.getPage(context.getViewerID());
    int buttonsSize = buttons.size();
    int slotsSize = slots.size();

    for (int i = 0; i < slotsSize; i++) {
      int index = i + basePage;
      if (this.cycle) {
        index = this.getExactPage(index, pageAmount);
      } else if (index >= buttonsSize) {
        break;
      }
      ActionItem actionItem = buttons.get(index).getItem(context);
      if (actionItem != null) {
        map.put(slots.get(i), actionItem);
      }
    }

    return map;
  }

  @Override
  public void stop() {
    this.pageNumberMap.clear();
  }
}
