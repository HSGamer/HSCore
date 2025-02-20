package me.hsgamer.hscore.minecraft.gui.mask;

import me.hsgamer.hscore.minecraft.gui.common.button.ButtonMap;
import me.hsgamer.hscore.minecraft.gui.common.inventory.InventoryContext;
import me.hsgamer.hscore.minecraft.gui.common.item.ActionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PaginatedMask implements ButtonMap {
  protected final Map<UUID, Integer> pageNumberMap = new ConcurrentHashMap<>();
  protected boolean cycle = false;

  /**
   * Generate the item map for the unique id
   *
   * @param context    the context
   * @param pageNumber the page number
   *
   * @return the map contains the slots and the buttons
   */
  protected abstract @Nullable Map<@NotNull Integer, @NotNull ActionItem> getItemMap(@NotNull InventoryContext context, int pageNumber);

  /**
   * Get the exact page from the input page
   *
   * @param page       the input page
   * @param pageAmount the amount of pages
   *
   * @return the exact page
   */
  protected int getExactPage(int page, int pageAmount) {
    if (pageAmount <= 0) {
      return 0;
    }

    int exactPage = page;
    if (this.cycle) {
      while (exactPage < 0) {
        exactPage += pageAmount;
      }
      exactPage = exactPage % pageAmount;
    } else if (exactPage < 0) {
      exactPage = 0;
    } else if (exactPage >= pageAmount) {
      exactPage = pageAmount - 1;
    }

    return exactPage;
  }

  /**
   * Get the exact page from the input page and set it if it's not the same
   *
   * @param uuid       the unique id
   * @param page       the input page
   * @param pageAmount the amount of pages
   *
   * @return the exact page
   */
  protected int getAndSetExactPage(UUID uuid, int page, int pageAmount) {
    int exactPage = getExactPage(page, pageAmount);

    if (exactPage != page) {
      this.setPage(uuid, exactPage);
    }

    return exactPage;
  }

  /**
   * Set the page for the unique id
   *
   * @param uuid the unique id
   * @param page the page
   */
  public void setPage(@NotNull UUID uuid, int page) {
    this.pageNumberMap.put(uuid, page);
  }

  /**
   * Get the current page for the unique id
   *
   * @param uuid the unique id
   *
   * @return the page number
   */
  public int getPage(@NotNull UUID uuid) {
    return pageNumberMap.computeIfAbsent(uuid, uuid1 -> 0);
  }

  /**
   * Set the next page for the unique id
   *
   * @param uuid the unique id
   */
  public void nextPage(@NotNull UUID uuid) {
    this.setPage(uuid, this.getPage(uuid) + 1);
  }

  /**
   * Set the previous page for the unique id
   *
   * @param uuid the unique id
   */
  public void previousPage(@NotNull UUID uuid) {
    this.setPage(uuid, this.getPage(uuid) - 1);
  }

  /**
   * Check if this paginated mask allows cycle page (The first page after the last page)
   *
   * @return true if it does
   */
  public boolean isCycle() {
    return cycle;
  }

  /**
   * Set if this paginated mask allows cycle page (The first page after the last page)
   *
   * @param cycle true if it does
   */
  public void setCycle(boolean cycle) {
    this.cycle = cycle;
  }

  @Override
  public @Nullable Map<Integer, ActionItem> getItemMap(@NotNull InventoryContext context) {
    return getItemMap(context, this.getPage(context.getViewerID()));
  }
}
