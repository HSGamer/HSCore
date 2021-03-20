package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.mask.BaseMask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PaginatedMask extends BaseMask {
  protected final Map<UUID, Integer> pageNumberMap = new ConcurrentHashMap<>();

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  protected PaginatedMask(String name) {
    super(name);
  }

  /**
   * Get the maximum page number
   *
   * @return the maximum page number
   */
  protected abstract int getMaxPage();

  /**
   * Set the page for the unique id
   *
   * @param uuid the unique id
   * @param page the page
   */
  public void setPage(UUID uuid, int page) {
    int maxPage = getMaxPage();
    if (page < 0) {
      page = 0;
    } else if (page > maxPage) {
      page = maxPage;
    }
    this.pageNumberMap.put(uuid, page);
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
}
