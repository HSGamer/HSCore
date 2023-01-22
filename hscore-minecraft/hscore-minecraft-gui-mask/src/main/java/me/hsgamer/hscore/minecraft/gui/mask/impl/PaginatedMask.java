package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PaginatedMask extends BaseMask {
  protected final Map<UUID, Integer> pageNumberMap = new ConcurrentHashMap<>();
  protected boolean cycle = false;

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  protected PaginatedMask(@NotNull String name) {
    super(name);
  }

  /**
   * Get the amount of pages for the unique id
   *
   * @param uuid the unique id
   *
   * @return the amount of pages
   */
  public abstract int getPageAmount(@NotNull UUID uuid);

  /**
   * Set the page for the unique id
   *
   * @param uuid the unique id
   * @param page the page
   */
  public void setPage(@NotNull UUID uuid, int page) {
    this.pageNumberMap.put(uuid, this.getExactPage(page, uuid));
  }

  /**
   * Get the exact page from the input page for the unique id
   *
   * @param page the input page
   * @param uuid the unique id
   *
   * @return the exact page
   */
  public int getExactPage(int page, @NotNull UUID uuid) {
    int pageAmount = this.getPageAmount(uuid);
    if (pageAmount <= 0) {
      return 0;
    }
    if (this.cycle) {
      while (page < 0) {
        page += pageAmount;
      }
      page = page % pageAmount;
    } else if (page < 0) {
      page = 0;
    } else if (page >= pageAmount) {
      page = pageAmount - 1;
    }
    return page;
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
}
