package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The mask paginated mask, those with a long list of {@link ButtonMap} divided into pages.
 */
public abstract class MaskPaginatedMask extends PaginatedMask {
  /**
   * Get the masks for the unique id
   *
   * @param uuid the unique id
   *
   * @return the masks
   */
  @NotNull
  public abstract List<@NotNull ButtonMap> getMasks(@NotNull UUID uuid);

  @Override
  protected @Nullable Map<@NotNull Integer, @NotNull ActionItem> getItemMap(InventoryContext context, int pageNumber) {
    List<ButtonMap> masks = getMasks(context.getViewerID());
    if (masks.isEmpty()) {
      return null;
    }
    int pageAmount = masks.size();
    pageNumber = getAndSetExactPage(context.getViewerID(), pageNumber, pageAmount);
    return masks.get(pageNumber).getItemMap(context);
  }

  @Override
  public void stop() {
    this.pageNumberMap.clear();
  }
}
