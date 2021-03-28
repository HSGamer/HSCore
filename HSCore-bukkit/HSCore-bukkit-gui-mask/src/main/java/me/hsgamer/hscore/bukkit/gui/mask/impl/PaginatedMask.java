package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.mask.BaseMask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PaginatedMask extends BaseMask {
  protected final Map<UUID, Integer> pageNumberMap = new ConcurrentHashMap<>();
  protected boolean cycle;

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  protected PaginatedMask(String name) {
    super(name);
  }

  /**
   * Get the amount of pages
   *
   * @return the amount of pages
   */
  protected abstract int getPageAmount();

  /**
   * Set the page for the unique id
   *
   * @param uuid the unique id
   * @param page the page
   */
  public void setPage(UUID uuid, int page) {
    this.pageNumberMap.put(uuid, this.getExactPage(page));
  }

  /**
   * Get the exact page from the input page
   *
   * @param page the input page
   *
   * @return the exact page
   */
  public int getExactPage(int page) {
    int pageAmount = this.getPageAmount();
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
  public int getPage(UUID uuid) {
    return pageNumberMap.computeIfAbsent(uuid, uuid1 -> 0);
  }

  /**
   * Set the next page for the unique id
   *
   * @param uuid the unique id
   */
  public void nextPage(UUID uuid) {
    this.setPage(uuid, this.getPage(uuid) + 1);
  }

  /**
   * Set the previous page for the unique id
   *
   * @param uuid the unique id
   */
  public void previousPage(UUID uuid) {
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
