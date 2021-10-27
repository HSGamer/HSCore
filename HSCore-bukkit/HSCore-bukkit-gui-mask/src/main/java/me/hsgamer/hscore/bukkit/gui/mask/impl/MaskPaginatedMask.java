package me.hsgamer.hscore.bukkit.gui.mask.impl;

import me.hsgamer.hscore.bukkit.gui.button.Button;
import me.hsgamer.hscore.bukkit.gui.mask.Mask;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The mask paginated mask, those with a long list of {@link Mask} divided into pages.
 */
public abstract class MaskPaginatedMask extends PaginatedMask {

  /**
   * Create a new mask
   *
   * @param name the name of the mask
   */
  protected MaskPaginatedMask(String name) {
    super(name);
  }

  /**
   * Get the masks for the unique id
   *
   * @param uuid the unique id
   *
   * @return the masks
   */
  public abstract List<Mask> getMasks(UUID uuid);

  @Override
  public Map<Integer, Button> generateButtons(UUID uuid) {
    List<Mask> masks = getMasks(uuid);
    return masks.isEmpty() ? Collections.emptyMap() : masks.get(this.getPage(uuid)).generateButtons(uuid);
  }

  @Override
  protected int getPageAmount(UUID uuid) {
    return getMasks(uuid).size();
  }

  @Override
  public void stop() {
    this.pageNumberMap.clear();
  }
}
