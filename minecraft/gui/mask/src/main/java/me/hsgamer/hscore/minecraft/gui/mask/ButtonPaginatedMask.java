package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.button.Button;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * The button paginated mask, those with a long list of {@link Button} divided into pages.
 */
public abstract class ButtonPaginatedMask extends PaginatedMask {
  private final Function<InventoryContext, List<Integer>> maskSlot;

  /**
   * Create a new mask
   *
   * @param maskSlot the mask slot
   */
  protected ButtonPaginatedMask(@NotNull Function<InventoryContext, List<Integer>> maskSlot) {
    this.maskSlot = maskSlot;
  }

  /**
   * Get the mask slot
   *
   * @return the mask slot
   */
  @NotNull
  public Function<InventoryContext, List<Integer>> getMaskSlot() {
    return maskSlot;
  }

  /**
   * Get the buttons for the unique id
   *
   * @param uuid the unique id
   *
   * @return the buttons
   */
  @NotNull
  public abstract List<@NotNull Button> getButtons(@NotNull UUID uuid);

  @Override
  protected @Nullable Map<@NotNull Integer, @NotNull ActionItem> getItemMap(@NotNull InventoryContext context, int pageNumber) {
    List<Integer> slots = this.maskSlot.apply(context);
    List<Button> buttons = getButtons(context.getViewerID());
    if (buttons.isEmpty() || slots.isEmpty()) {
      return null;
    }

    int pageAmount = (int) Math.ceil((double) buttons.size() / slots.size());
    pageNumber = this.getAndSetExactPage(context.getViewerID(), pageNumber, pageAmount);

    Map<Integer, ActionItem> map = new HashMap<>();
    int slotsSize = slots.size();
    int offset = pageNumber * slotsSize;
    int buttonsSize = buttons.size();

    for (int i = 0; i < slotsSize; i++) {
      int index = i + offset;
      if (index >= buttonsSize) {
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
