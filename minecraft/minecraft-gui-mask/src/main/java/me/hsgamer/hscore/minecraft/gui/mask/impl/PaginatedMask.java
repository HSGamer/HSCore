package me.hsgamer.hscore.minecraft.gui.mask.impl;

import me.hsgamer.hscore.minecraft.gui.button.Button;
import me.hsgamer.hscore.minecraft.gui.mask.BaseMask;
import me.hsgamer.hscore.minecraft.gui.object.InventorySize;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
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
   * Generate the buttons for the unique id
   *
   * @param uuid          the unique id
   * @param inventorySize the size of the inventory
   * @param pageNumber    the page number
   *
   * @return the map contains the slots and the buttons
   *
   * @see me.hsgamer.hscore.minecraft.gui.mask.Mask#generateButtons(UUID, InventorySize)
   */
  protected abstract Optional<Map<@NotNull Integer, @NotNull Button>> generateButtons(@NotNull UUID uuid, @NotNull InventorySize inventorySize, int pageNumber);

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
  public Optional<Map<@NotNull Integer, @NotNull Button>> generateButtons(@NotNull UUID uuid, @NotNull InventorySize inventorySize) {
    return generateButtons(uuid, inventorySize, this.getPage(uuid));
  }
}
